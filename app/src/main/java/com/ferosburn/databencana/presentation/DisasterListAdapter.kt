package com.ferosburn.databencana.presentation

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ferosburn.databencana.R
import com.ferosburn.databencana.data.DisasterModel
import com.ferosburn.databencana.databinding.DisasterListItemBinding
import com.ferosburn.databencana.util.disasterValueToDisasterTypes
import com.ferosburn.databencana.util.provinceCodeToProvinces
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DisasterListAdapter :
    ListAdapter<DisasterModel, DisasterListAdapter.DisasterListViewHolder>(DiffCallback) {
    class DisasterListViewHolder(
        private var binding: DisasterListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(disasterItem: DisasterModel) {
            binding.apply {
                ivDisaster.load(disasterItem.imageUrl) {
                    placeholder(R.drawable.ic_disaster_placeholder)
                    error(R.drawable.ic_disaster_placeholder)
                }
                tvDisasterCoordinates.text = disasterItem.coordinates.let { "${it[0]}\n${it[1]}" }
                tvDisasterTime.text = LocalDateTime.parse(
                    disasterItem.createdAt,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                )?.let { "${it.dayOfWeek}, ${it.dayOfMonth} ${it.month} ${it.year}\n${it.hour}:${it.minute}" }
                tvDisasterType.text =
                    disasterItem.disasterType.disasterValueToDisasterTypes()?.disasterName
                tvProvince.text =
                    disasterItem.instanceRegionCode.provinceCodeToProvinces()?.provinceName
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DisasterModel>() {
        override fun areItemsTheSame(oldItem: DisasterModel, newItem: DisasterModel): Boolean {
            return oldItem.pkey == newItem.pkey
        }

        override fun areContentsTheSame(oldItem: DisasterModel, newItem: DisasterModel): Boolean {
            return oldItem.coordinates == newItem.coordinates
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisasterListViewHolder {
        return DisasterListViewHolder(DisasterListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DisasterListViewHolder, position: Int) {
        val disasterItem = getItem(position)
        holder.bind(disasterItem)
    }
}