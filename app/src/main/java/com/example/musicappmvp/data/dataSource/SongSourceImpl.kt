package com.example.musicappmvp.data.dataSource

import com.example.musicappmvp.data.model.Song
import com.example.musicappmvp.R
import com.example.musicappmvp.screen.listSong.presenter.OnResultListener

class SongSourceImpl: SongSource {
    override fun getSongs(listener: OnResultListener<List<Song>>) {
        val list = mutableListOf<Song>()
        list.add(Song("Mộng đẹp hóa thành hoa","author 1", R.raw.mong_dep_hoa_thanh_hoa,"https://photo-resize-zmp3.zmdcdn.me/w600_r1x1_jpeg/cover/0/e/4/6/0e46f7a99892f3c461192806b7b1b043.jpg"))
        list.add(Song("Phía sau một cô gái","author 1",R.raw.phia_sau_mot_co_gai,""))
        list.add(Song("Trách phận vô danh","author 1",R.raw.trach_phan_vo_danh,"https://i1.sndcdn.com/artworks-IlBEQUJ2brPW-0-t500x500.jpg"))
        list.add(Song("Vở kịch của em remix","author 1",R.raw.vo_kich_cua_em,"https://photo-resize-zmp3.zmdcdn.me/w600_r1x1_jpeg/cover/c/b/f/c/cbfce855637795be89b9a722153ee85b.jpg"))
        list.add(Song("Xin lỗi vì đã xuất hiện","author 1",R.raw.xin_loi_vi_da_xuat_hien,"https://photo-resize-zmp3.zmdcdn.me/w600_r1x1_jpeg/cover/7/1/f/d/71fd0c35ed6b0ca43eae5fc57e7983a9.jpg"))
        listener.onSuccess(list)
    }
}