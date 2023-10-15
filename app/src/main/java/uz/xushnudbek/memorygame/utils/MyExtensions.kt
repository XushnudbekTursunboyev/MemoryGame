package uz.xushnudbek.memorygame.utils

import android.view.View
import android.widget.ImageView
import uz.xushnudbek.memorygame.R
import uz.xushnudbek.memorygame.data.CardData


fun ImageView.openCard(finishAnim : () -> Unit = {}) {
    this.animate()
        .setDuration(400)
        .rotationY(89f)
        .withEndAction {
            this.setImageResource((tag as CardData).img)
            this.rotationY = -91f
            this.animate()
                .setDuration(400)
                .rotationY(0f)
                .withEndAction {
                    finishAnim.invoke()
                }.start()
        }
        .start()
}


fun ImageView.closeCard(finishAnim : () -> Unit = {}) {
    this.animate()
        .setDuration(400)
        .rotationY(-91f)
        .withEndAction {
            this.setImageResource(R.drawable.image_back)
            this.rotationY = 89f
            this.animate()
                .setDuration(400)
                .rotationY(0f)
                .withEndAction {
                    finishAnim.invoke()
                }.start()
        }
        .start()
}


fun ImageView.hideCard(finishAnim : () -> Unit = {}) {
    this.animate()
        .setDuration(1000)
        .scaleX(0f)
        .scaleY(0f)
        .withEndAction {
            this.visibility = View.GONE
            finishAnim.invoke()
        }
        .start()
}