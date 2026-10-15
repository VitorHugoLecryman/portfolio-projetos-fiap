package br.com.fiap.inovagab.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.inovagab.R
import br.com.fiap.inovagab.model.Project
import java.text.SimpleDateFormat
import java.util.Locale

class ProjectAdapter(
    private var projetos: List<Project>,
    private val isReadOnly: Boolean = false,
    private val onAlterarEtapa: ((Project) -> Unit)? = null,
    private val onAdicionarResultados: ((Project) -> Unit)? = null
) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTituloProjeto)
        val tvDescricao: TextView = view.findViewById(R.id.tvDescricaoProjeto)
        val tvEtapa: TextView = view.findViewById(R.id.tvEtapaProjeto)
        val tvOrcamento: TextView = view.findViewById(R.id.tvOrcamentoProjeto)
        val tvGestor: TextView = view.findViewById(R.id.tvGestorProjeto)
        val tvRetorno: TextView = view.findViewById(R.id.tvRetornoProjeto)
        val btnAlterarEtapa: Button = view.findViewById(R.id.btnAlterarEtapa)
        val btnResultados: Button = view.findViewById(R.id.btnResultados)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val projeto = projetos[position]
        holder.tvTitulo.text = projeto.titulo
        holder.tvDescricao.text = projeto.descricao
        holder.tvEtapa.text = projeto.etapa
        holder.tvOrcamento.text = "Orçamento: R$ %.2f".format(projeto.orcamento)
        holder.tvGestor.text = if (projeto.orientacaoTitulo.isBlank()) {
            "Gestor: ${projeto.gestorNome}"
        } else {
            "Gestor: ${projeto.gestorNome} · ${projeto.orientacaoTitulo}"
        }

        if (projeto.retornoFinanceiro > 0) {
            holder.tvRetorno.visibility = View.VISIBLE
            holder.tvRetorno.text = ("Retorno: R$ %.2f | Produtividade: %.1f%%" +
                "\nLucro: R$ %.2f | ROI: %.1f%%").format(
                projeto.retornoFinanceiro, projeto.ganhoProdutividade, projeto.lucro, projeto.roi
            )
        } else {
            holder.tvRetorno.visibility = View.GONE
        }

        if (isReadOnly) {
            holder.btnAlterarEtapa.visibility = View.GONE
            holder.btnResultados.visibility = View.GONE
        } else {
            // Mostrar botão de alterar etapa apenas se não estiver concluído/cancelado
            val podeAlterar = projeto.etapa == "Planejamento" || projeto.etapa == "Em Andamento"
            holder.btnAlterarEtapa.visibility = if (podeAlterar) View.VISIBLE else View.GONE
            holder.btnAlterarEtapa.setOnClickListener { onAlterarEtapa?.invoke(projeto) }

            // Registrar resultados conclui o projeto, então vale enquanto ele não foi cancelado
            val podeConcluir = projeto.etapa != "Cancelado" && projeto.retornoFinanceiro == 0.0
            holder.btnResultados.visibility = if (podeConcluir) View.VISIBLE else View.GONE
            holder.btnResultados.setOnClickListener { onAdicionarResultados?.invoke(projeto) }
        }
    }

    override fun getItemCount() = projetos.size

    fun atualizarLista(novaLista: List<Project>) {
        projetos = novaLista
        notifyDataSetChanged()
    }
}
