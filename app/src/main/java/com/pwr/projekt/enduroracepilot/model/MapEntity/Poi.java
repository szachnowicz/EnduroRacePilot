package com.pwr.projekt.enduroracepilot.model.MapEntity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.pwr.projekt.enduroracepilot.R;

/**
 * Created by Sebastian on 2017-04-26.
 */

public enum Poi {
    USKOK(R.raw.uskok, R.drawable.drop_icon, R.string.uskok),
    SCHODY(R.raw.schody, R.drawable.stairs_right_icon, R.string.schody),
    WASKI_NAWROT_LEWO(R.raw.waskil, R.drawable.turn_back_left_icon, R.string.waskiNawrotLewo),
    WASKI_NAWROT_PRAWO(R.raw.waskir, R.drawable.turn_back_right_icon, R.string.waskiNawrotPrawo),
    SKARPA_Z_LEWEJ(R.raw.skarpal, R.drawable.levee_left_icon, R.string.skarpaZLewej),
    SKARPA_Z_PRAWEK(R.raw.skarpar, R.drawable.levee_right_icon, R.string.skarpazPrawiej),
    PRZESZKODA_DO_PRZESKOCZENIA(R.raw.skarpar, R.drawable.obstacle_jump_icon, R.string.skarpazPrawiej),
    SZUTE_ENDURO(R.raw.szuter, R.drawable.gravel_icon, R.string.szuterEndruo),
    DUZE_WYSTROMIENIE(R.raw.wystromienie, R.drawable.steep_icon, R.string.duzeWystromieni),
    SZYBKO_NA_ODCINKU(R.raw.szybko, R.drawable.fast_icon, R.string.szybkoNaOdciku),
    UWAGA(R.raw.uwazaj, R.drawable.caution_icon, R.string.uwazaj);
    int sound;
    int drawable;
    int discription;

    Poi(int sound, int texture, int opis) {
        this.sound = sound;
        this.drawable = texture;
        this.discription = opis;

    }

    public void playSound(Context context) {
        MediaPlayer mMediaPlayer = MediaPlayer.create(context, this.sound);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.start();

    }

    public int getSound() {
        return sound;
    }

    public int getDrawable() {
        return drawable;
    }

    public int getDiscription() {
        return discription;
    }

}
