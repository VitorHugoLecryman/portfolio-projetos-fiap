package br.com.fiap.inovagab.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.inovagab.R
import br.com.fiap.inovagab.model.Idea
import java.text.SimpleDateFormat
import java.util.Locale

class IdeaAdapter(
    private var ideias: List<Idea>
) : RecyclerView.Adapter<IdeaAdapter.IdeaViewHolder>() {

    class IdeaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTituloIdeia)
        val tvDescricao: TextView = view.findViewById(R.id.tvDescricaoIdeia)
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoriaIdeia)
        val tvStatus: TextView = view.findViewById(R.id.tvStatusIdeia)
        val tvData: TextView = view.findViewById(R.id.tvDataIdeia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_idea, parent, false)
        return IdeaViewHolder(view)
    }

    override fun onBindViewHolder(holder: IdeaViewHolder, position: Int) {
        val idea = ideias[position]
        holder.tvTitulo.text = idea.titulo
        holder.tvDescricao.text = idea.descricao
        holder.tvCategoria.text = idea.categoria
        holder.tvStatus.text = idea.status

        val cor = when (idea.status) {
            "Aprovada" -> ContextCompat.getColor(holder.itemView.context, R.color.status_aprovada)
            "Rejeitada" -> ContextCompat.getColor(holder.itemView.context, R.color.status_rejeitada)
            else -> ContextCompat.getColor(holder.itemView.context, R.color.status_enviada)
        }
        holder.tvStatus.setTextColor(cor)

        idea.dataEnvio?.let {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
            holder.tvData.text = sdf.format(it)
        }
    }

    override fun getItemCount() = ideias.size

    fun atualizarLista(novaLista: List<Idea>) {
        ideias = novaLista
        notifyDataSetChanged()
    }
}
