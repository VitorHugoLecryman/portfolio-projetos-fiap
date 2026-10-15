package br.com.fiap.inovagab.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.inovagab.R

class RankingAdapter(
    private var ranking: List<Map<String, Any>>
) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    class RankingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPosicao: TextView = view.findViewById(R.id.tvPosicao)
        val tvNome: TextView = view.findViewById(R.id.tvNomeRanking)
        val tvPontos: TextView = view.findViewById(R.id.tvPontosRanking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val item = ranking[position]
        val posicao = (item["posicao"] as? Number)?.toInt() ?: (position + 1)
        holder.tvPosicao.text = "${posicao}º"
        holder.tvNome.text = item["nome"] as? String ?: ""
        holder.tvPontos.text = "${item["pontos"]} pts"
    }

    override fun getItemCount() = ranking.size

    fun atualizarLista(novaLista: List<Map<String, Any>>) {
        ranking = novaLista
        notifyDataSetChanged()
    }
}
