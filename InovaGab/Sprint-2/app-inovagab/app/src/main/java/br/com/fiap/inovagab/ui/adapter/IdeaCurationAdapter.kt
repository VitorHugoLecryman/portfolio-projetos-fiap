package br.com.fiap.inovagab.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.inovagab.R
import br.com.fiap.inovagab.model.Idea
import java.text.SimpleDateFormat
import java.util.Locale

class IdeaCurationAdapter(
    private var ideias: List<Idea>,
    private val onAprovar: (Idea, String, String) -> Unit,
    private val onRejeitar: (Idea, String) -> Unit,
    private val onAvaliarIa: ((Idea) -> Unit)? = null
) : RecyclerView.Adapter<IdeaCurationAdapter.CurationViewHolder>() {

    class CurationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTituloIdeia)
        val tvDescricao: TextView = view.findViewById(R.id.tvDescricaoIdeia)
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoriaIdeia)
        val tvAutor: TextView = view.findViewById(R.id.tvAutorIdeia)
        val tvData: TextView = view.findViewById(R.id.tvDataIdeia)
        val tvOrientacao: TextView = view.findViewById(R.id.tvOrientacaoIdeia)
        val tvAvaliacaoIa: TextView = view.findViewById(R.id.tvAvaliacaoIa)
        val btnAvaliarIa: Button = view.findViewById(R.id.btnAvaliarIa)
        val btnAprovar: Button = view.findViewById(R.id.btnAprovar)
        val btnRejeitar: Button = view.findViewById(R.id.btnRejeitar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_idea_curation, parent, false)
        return CurationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurationViewHolder, position: Int) {
        val idea = ideias[position]
        holder.tvTitulo.text = idea.titulo
        holder.tvDescricao.text = idea.descricao
        holder.tvCategoria.text = idea.categoria
        holder.tvAutor.text = "Por: ${idea.autorNome}"

        idea.dataEnvio?.let {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
            holder.tvData.text = sdf.format(it)
        }

        holder.tvOrientacao.text = if (idea.orientacaoTitulo.isBlank()) {
            ""
        } else {
            "Orientação: ${idea.orientacaoTitulo}"
        }

        val prioridade = idea.prioridade
        if (prioridade != null) {
            holder.tvCategoria.text = "${idea.categoria} · prioridade $prioridade"
        }

        val score = idea.scoreIa
        if (score != null) {
            holder.tvAvaliacaoIa.visibility = View.VISIBLE
            holder.tvAvaliacaoIa.text = "IA: $score/100 — ${idea.justificativaIa}"
        } else {
            holder.tvAvaliacaoIa.visibility = View.GONE
        }

        holder.btnAvaliarIa.visibility = if (onAvaliarIa == null) View.GONE else View.VISIBLE
        holder.btnAvaliarIa.setOnClickListener { onAvaliarIa?.invoke(idea) }

        holder.btnAprovar.setOnClickListener {
            mostrarDialogAprovacao(holder.itemView, idea)
        }

        holder.btnRejeitar.setOnClickListener {
            mostrarDialogRejeicao(holder.itemView, idea)
        }
    }

    private fun mostrarDialogAprovacao(view: View, idea: Idea) {
        val dialogView = LayoutInflater.from(view.context)
            .inflate(R.layout.dialog_aprovar_ideia, null)

        val rgImpacto = dialogView.findViewById<RadioGroup>(R.id.rgImpacto)
        val rgEsforco = dialogView.findViewById<RadioGroup>(R.id.rgEsforco)

        AlertDialog.Builder(view.context)
            .setTitle("Avaliar Ideia")
            .setView(dialogView)
            .setPositiveButton("Aprovar") { _, _ ->
                onAprovar(idea, nivelSelecionado(rgImpacto), nivelSelecionado(rgEsforco))
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun nivelSelecionado(grupo: RadioGroup): String = when (grupo.checkedRadioButtonId) {
        R.id.rbImpactoAlto, R.id.rbEsforcoAlto -> "Alto"
        R.id.rbImpactoMedio, R.id.rbEsforcoMedio -> "Médio"
        else -> "Baixo"
    }

    private fun mostrarDialogRejeicao(view: View, idea: Idea) {
        val etMotivo = EditText(view.context).apply {
            hint = "Motivo da rejeição"
            setPadding(48, 32, 48, 0)
        }

        AlertDialog.Builder(view.context)
            .setTitle("Rejeitar Ideia")
            .setView(etMotivo)
            .setPositiveButton("Rejeitar") { _, _ ->
                onRejeitar(idea, etMotivo.text.toString().trim())
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun getItemCount() = ideias.size

    fun atualizarLista(novaLista: List<Idea>) {
        ideias = novaLista
        notifyDataSetChanged()
    }
}
