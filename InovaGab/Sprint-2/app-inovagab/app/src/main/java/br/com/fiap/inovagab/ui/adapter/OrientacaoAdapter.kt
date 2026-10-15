package br.com.fiap.inovagab.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.inovagab.R
import br.com.fiap.inovagab.model.StrategicOrientation
import java.text.SimpleDateFormat
import java.util.Locale

class OrientacaoAdapter(
    private var orientacoes: List<StrategicOrientation>,
    private val isReadOnly: Boolean = true,
    private val onEditar: ((StrategicOrientation) -> Unit)? = null,
    private val onExcluir: ((StrategicOrientation) -> Unit)? = null
) : RecyclerView.Adapter<OrientacaoAdapter.OrientacaoViewHolder>() {

    class OrientacaoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTituloOrientacao)
        val tvDescricao: TextView = view.findViewById(R.id.tvDescricaoOrientacao)
        val tvAutor: TextView = view.findViewById(R.id.tvAutorOrientacao)
        val tvData: TextView = view.findViewById(R.id.tvDataOrientacao)
        val btnEditar: ImageButton = view.findViewById(R.id.btnEditarOrientacao)
        val btnExcluir: ImageButton = view.findViewById(R.id.btnExcluirOrientacao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrientacaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_orientacao, parent, false)
        return OrientacaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrientacaoViewHolder, position: Int) {
        val orientacao = orientacoes[position]
        holder.tvTitulo.text = orientacao.titulo
        holder.tvDescricao.text = orientacao.descricao
        holder.tvAutor.text = if (orientacao.categoria.isBlank()) {
            "Por: ${orientacao.autorNome}"
        } else {
            "Por: ${orientacao.autorNome} · ${orientacao.categoria}"
        }

        orientacao.dataCriacao?.let {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
            holder.tvData.text = if (orientacao.campanha.isBlank()) {
                sdf.format(it)
            } else {
                "${sdf.format(it)} · ${orientacao.campanha}"
            }
        }

        if (isReadOnly) {
            holder.btnEditar.visibility = View.GONE
            holder.btnExcluir.visibility = View.GONE
        } else {
            holder.btnEditar.setOnClickListener { onEditar?.invoke(orientacao) }
            holder.btnExcluir.setOnClickListener { onExcluir?.invoke(orientacao) }
        }
    }

    override fun getItemCount() = orientacoes.size

    fun atualizarLista(novaLista: List<StrategicOrientation>) {
        orientacoes = novaLista
        notifyDataSetChanged()
    }
}
