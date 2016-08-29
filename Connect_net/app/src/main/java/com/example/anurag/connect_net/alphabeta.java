package com.example.anurag.connect_net;

import java.util.HashMap;

/**
 * Created by kapil on 18/7/16.
 */
public class alphabeta {

    //default player code
    public static int PLAYERCODE=1;

    private  int EvaluationFunc(int[][] board)
    {int score=0;
        //score for disk count
        score+=getDiskCountScore(board,PLAYERCODE);
        //score for corner disk count
        score+=getCornerDiskScore(board,PLAYERCODE);
        //score for mobility -higher mobility option higher score
        score+=getMobilityScore(board,PLAYERCODE);
        //score for stable disk count -disks that cant be changed
        score+=getStableDiskScore(board,PLAYERCODE);
        //score for the enmpty square whch can be used to bracket oppo disk
        score+=getPotentialMobilityScore(board,PLAYERCODE);

        return score;

    }
    private int getDiskCountScore(int[][] board,int playercode)
    {
        int diskcount=0;
        for(int i=0;i<=9;i++)
        {
            for(int j=0;j<=9;j++)
            {if(board[i][j]==playercode)
                diskcount++;

            }
        }
        return diskcount;

    }
    //retuens opposite playercode
    private int getOppositeCode(int playercode)
    {
        return (playercode+1)%2;
    }

    private int getCornerDiskScore(int[][]board, int playercode)
    {int score=0;
        int oppoplayercode=getOppositeCode(playercode);
        if(board[0][0]==playercode)
        score+=1000;
        else if(board[0][0]==oppoplayercode)
            score-=1000;



        if(board[0][9]==playercode)
            score+=1000;
        else if(board[0][9]==oppoplayercode)
            score-=1000;

        if(board[9][0]==playercode)
            score+=1000;
        else if(board[9][0]==oppoplayercode)
            score-=1000;

        if(board[9][9]==playercode)
            score+=1000;
        else if(board[9][9]==oppoplayercode)
            score-=1000;

        return score;
    }

    //return score on mobility
    private int getMobilityScore(int[][]board, int playercode){

int mobilityCount=0;
        for(int i=0;i<=9;i++)
        {
            for(int j=0;j<=9;j++)
            {if(board[i][j]==0)
                mobilityCount++;

            }
        }
        return mobilityCount*100;

    }

    private HashMap getHashmapObject()
    {return new HashMap<Integer,Integer>();}

    //return score baesed on stable disk
    private int getStableDiskScore(int[][]board, int playercode){
        int stablediskCount=0;
        HashMap<Integer,Integer> hash=getHashmapObject();

        if(board[0][0]==playercode)
            if(!hash.containsKey(0*10+0))
            {hash.put(0*10+0,1);
                stablediskCount++;
                getCorrespondingStableDisk(hash,board,0,0);
            }
        else if(board[0][0]==oppoplayercode)
            score-=1000;



        if(board[0][9]==playercode)
            score+=1000;
        else if(board[0][9]==oppoplayercode)
            score-=1000;

        if(board[9][0]==playercode)
            score+=1000;
        else if(board[9][0]==oppoplayercode)
            score-=1000;

        if(board[9][9]==playercode)
            score+=1000;
        else if(board[9][9]==oppoplayercode)
            score-=1000;


    }

}
