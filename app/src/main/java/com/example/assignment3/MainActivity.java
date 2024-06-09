package com.example.assignment3;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView scoreTextView, winner_loser;
    int userScore = 0, compScore = 0;
    Random random;                                  // Select a random number for the computer
    private boolean gameActive = true;              // checks if the game is over or not
    Button rockButton, paperButton, spearButton;    // Buttons for player selection
    ImageView playerImage, comImage;                // Images for player selection
    MediaPlayer winnerSound, loserSound;            // Sounds for winner or loser

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        winner_loser = findViewById(R.id.winner_loser);
        scoreTextView = findViewById(R.id.scoreTextView);

        rockButton = findViewById(R.id.rockButton);
        paperButton = findViewById(R.id.paperButton);
        spearButton = findViewById(R.id.spearButton);

        // Setting the text to nothing for now. Will be replaced by computer selection
        winner_loser.setText("");

        random = new Random();

        playerImage = findViewById(R.id.playerImage);
        comImage = findViewById(R.id.compImage);

        winnerSound = MediaPlayer.create(MainActivity.this, R.raw.successaudio);
        loserSound = MediaPlayer.create(MainActivity.this, R.raw.failaudio);
    }

    // This button selection turns each button into a numeric value so that it is easier to make assumptions for player and computer
    public void buttonSelection(View view) {
        if (!gameActive) {
            return;
        }
        int userSelection = Integer.parseInt(view.getTag().toString());
        Log.i(TAG, "buttonSelection: " + userSelection);
        matchGame(userSelection);
    }

    // Logic to determine who gets a point
    // rock = 1
    // paper = 2
    // spear = 3
    private void matchGame(int userSelection){
        // Computer random selection of a number between 1-3
        int comSelection = random.nextInt(3)+1;
        winner_loser.setText("");

        // if statements to check if winner or a tie
        if (userSelection == comSelection) {
            //tie
            winner_loser.setText("Draw");
        } else if ((userSelection == 1 && comSelection == 3) ||
                (userSelection == 2 && comSelection == 1) ||
                (userSelection == 3 && comSelection == 2)) {
            //e.g. rock beats spear; paper beats rock; spear beats paper
            //User wins!
            userScore++;
        } else {
            //comp wins
            compScore++;
        }

        // Call the set score function
        setScoreTextView(userScore, compScore);

        // Call function to set image based on user selection
        setImage(playerImage, userSelection);
        setImage(comImage, comSelection);

        // Call gameOver to finish the game
        gameOver(userScore, compScore);
    }

    private void setImage(ImageView imageView, int selection){
        switch(selection) {
            case 1:
                imageView.setImageResource(R.drawable.rock);
                break;
            case 2:
                imageView.setImageResource(R.drawable.paper);
                break;
            case 3:
                imageView.setImageResource(R.drawable.spear);
                break;
        }
    }

    // Setting the score on the screen
    private void setScoreTextView(int userScore, int compScore){
        scoreTextView.setText(String.valueOf(userScore) + " : " + String.valueOf(compScore));
    }

    // Ends the game when player reached 2 out of 3
    private void gameOver(int userScore, int compScore) {
        if (userScore == 2 || compScore == 2) {
            // we make the game "inactive" so it does not continue working
            gameActive = false;
            Log.i(TAG, "Game Over!");

            // disables the buttons so they do not work when the game is over.
            rockButton.setEnabled(false);
            paperButton.setEnabled(false);
            spearButton.setEnabled(false);

            if (userScore == 2) {
                winner_loser.setText("Sir Droid-a-lot has defeated the tricky Merlin, Good Job!");
                winnerSound.start();
            } else {
                winner_loser.setText("Sir Droid-a-lot's quest was not successful.");
                loserSound.start();
            }
        }
    }
    // Setting the reset button
    public void resetButton(View view) {
        winner_loser.setText("");
        userScore = 0;
        compScore = 0;
        setScoreTextView(userScore, compScore);

        // Clear images to restart the game
        playerImage.setImageResource(0);
        comImage.setImageResource(0);

        // we set the game as active again
        gameActive = true;

        //re-enable the play buttons
        rockButton.setEnabled(true);
        paperButton.setEnabled(true);
        spearButton.setEnabled(true);
    }
}