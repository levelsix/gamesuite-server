package com.lvl6.pictures.properties;

public class PicturesPoConstants {

    //user

    //currency
    public static int CURRENCY__MINUTES_FOR_TOKEN_REGENERATION = 3;
    public static int CURRENCY__SECONDS_FOR_TOKEN_REGENERATION = 180;
    public static int CURRENCY__DEFAULT_MAX_TOKENS = 10;
    public static int CURRENCY__DEFAULT_INITIAL_TOKENS = 10;
    public static int CURRENCY__DEFAULT_INITIAL_RUBIES = 20;

    //game history
    public static int GAME_HISTORY__DEFAULT_NUM_DISPLAYED_GAMES = 5;  //might not be used
    public static int GAME_HISTORY__DEFAULT_COMPLETED_GAMES_MIN_DAYS_DISPLAYED= 2; //might not be used

    //round history
    public static int ROUND_HISTORY__DEFAULT_ROUNDS_PER_PLAYER_PER_GAME = 3;
    public static int ROUND_HISTORY__DEFAULT_MINUTES_PER_ROUND = 2;

    //question base
    public static int QUESTION_BASE__DEFAULT_NUM_QUESTIONS_TO_GET = 100;

    //multiple choice question
    public static int MCQ__POINTS_FOR_CORRECT_ANSWER = 30;
    public static int MCQ__POINTS_FOR_INCORRECT_ANSWER = 0;

    //pictures question or answer construction question
    public static int ACQ__POINTS_FOR_CORRECT_ANSWER = 40;
    public static int ACQ__POINTS_FOR_INCORRECT_ANSWER = 0;
}