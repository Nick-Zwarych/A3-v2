package com.example.assignment3.model;

public class MovieModel {

    String movieImgUrl;
    String movieName;
    String movieYear;
    String moviePlot;

    // add back imgURl + description later (if needed)
    public MovieModel(String movieImgUrl, String movieName, String movieYear, String moviePlot){
        this.movieImgUrl = movieImgUrl;
        this.movieName = movieName;
        this.movieYear = movieYear;
        this.moviePlot = moviePlot;
    }

    // didn't end up needing set methods in assignment 2 - unsure about assignment 3 so far...

    public String getMovieImgUrl() {return movieImgUrl;}
    //public void setMovieImgUrl(String movieImgUrl) {this.movieImgUrl = movieImgUrl;}

    public String getMovieName() {return movieName;}
    //public void setMovieName(String movieName) {this.movieName = movieName;}

    public String getMovieYear() {return movieYear;}
    //public void setMovieYear(String movieYear) {this.movieYear = movieYear;}

    public String getMoviePlot() {return moviePlot;}
    //public void setMoviePlot(String movieDescription) {this.moviePlot = movieDescription;}

}
