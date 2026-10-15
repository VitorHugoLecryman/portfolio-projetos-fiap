package br.com.fiap.inovagab.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.inovagab.R
import br.com.fiap.inovagab.model.PointTransaction
import java.text.SimpleDateFormat
import java.util.Locale

class PointsHistoryAdapter(
    private var transacoes: List<PointTransaction>
) : RecyclerView.Adapter<PointsHistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMotivo: TextView = view.findViewById(R.id.tvMotivoPontos)
        val tvPontos: TextView = view.findViewById(R.id.tvQuantidadePontos)
        val tvData: TextView = view.findViewById(R.id.tvDataPontos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_points_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val transacao = transacoes[position]
        holder.tvMotivo.text = transacao.motivo
        holder.tvPontos.text = "+${transacao.pontos} pts"

        transacao.data?.let {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
            holder.tvData.text = sdf.format(it)
        }
    }

    override fun getItemCount() = transacoes.size

    fun atualizarLista(novaLista: List<PointTransaction>) {
        transacoes = novaLista
        notifyDataSetChanged()
    }
}
