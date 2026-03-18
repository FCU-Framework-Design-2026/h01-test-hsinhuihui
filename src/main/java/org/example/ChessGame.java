package org.example;

import java.util.ArrayList;
import java.util.Collections;

class ChessGame extends AbstractGame {
    Chess[] board = new Chess[32];
    int Player1_side = -1;
    int turn = 0;

    public void setPlayers(String Player1, String Player2) {
        System.out.println("遊戲開始！" + '\n' + Player1 + " vs " + Player2);
    }

    public boolean gameOver() {
        int red = 0;
        int black = 0;

        for (Chess chess : board) {
            if (chess.side == 0) red++;
            else if (chess.side == 1) black++;
        }

        if (red == 0) {
            System.out.println("遊戲結束！黑方獲勝！");
            return true;
        }

        if (black == 0) {
            System.out.println("遊戲結束！紅方獲勝！");
            return true;
        }
        return false;
    }

    public boolean move(int now_location, int target_location) {
        if (!board[target_location].name.equals("＿")) System.out.println(((board[now_location].side == 0) ? "(紅方) " : "(黑方) ") + board[now_location].name + " 吃掉 " + ((board[target_location].side == 0) ? "(紅方) " : "(黑方) ") + board[target_location].name);
        board[target_location] = board[now_location];
        board[now_location] = new Chess("＿", 0, -1, now_location);
        board[now_location].isOpened = true; // 因為new，isOpened != true就會被顯示 X
        return true;
    }

    public void showAllChess() {
        char row = 'A';
        System.out.println("\t 1\t 2\t 3\t 4\t 5\t 6\t 7\t 8");
        for (int i = 0; i < 4; i++) {
            System.out.print(row + "\t");
            for (int j = 0; j < 8; j++) {
                System.out.print(board[j+i*8] + "\t");
            }
            System.out.println();
            row += 1;
        }
        System.out.println();
    }

    public void generateChess() {
        ArrayList<Chess> allList = new ArrayList<>();

        // 紅方 (side = 0)
        allList.add(new Chess("帥", 7, 0, -1));
        for (int i = 0; i < 2; i++) {
            allList.add(new Chess("仕", 6, 0, -1));
            allList.add(new Chess("相", 5, 0, -1));
            allList.add(new Chess("俥", 4, 0, -1));
            allList.add(new Chess("傌", 3, 0, -1));
            allList.add(new Chess("炮", 2, 0, -1));
        }

        for (int i = 0; i < 5; i++) {
            allList.add(new Chess("兵", 1, 0, -1));
        }

        // 黑方 (side = 1)
        allList.add(new Chess("將", 7, 1, -1));
        for (int i = 0; i < 2; i++) {
            allList.add(new Chess("士", 6, 1, -1));
            allList.add(new Chess("象", 5, 1, -1));
            allList.add(new Chess("車", 4, 1, -1));
            allList.add(new Chess("馬", 3, 1, -1));
            allList.add(new Chess("砲", 2, 1, -1));
        }

        for (int i = 0; i < 5; i++) {
            allList.add(new Chess("卒", 1, 1, -1));
        }

        Collections.shuffle(allList);

        for (int i = 0; i < 32; i++) {
            board[i] = allList.get(i);
            board[i].location = i;
        }
    }

    // 選擇砲/炮吃棋子，中間需隔一個棋子
    public int countBetweenChess(int now_location, int target_location) {
        int chessCount = 0;
        int now_row = now_location / 8, now_col = now_location % 8;
        int target_row = target_location / 8, target_col = target_location % 8;

        if (now_col == target_col) { // 上、下跳
            int min_row = Math.min(now_row, target_row);
            int max_row = Math.max(now_row, target_row);

            for (int i = min_row+1; i < max_row; i++) {
                if (!board[8 * i + now_col].name.equals("＿")) chessCount++;
            }
        }
        else if (now_row == target_row) { // 左、右跳
            int min_col = Math.min(now_col, target_col);
            int max_col = Math.max(now_col, target_col);

            for (int i = min_col+1; i < max_col; i++) {
                if (!board[now_row * 8 + i].name.equals("＿")) chessCount++;
            }
        }
        return chessCount;
    }

    // 切換回合
    public void nextTurn() {
        turn = (turn == 0) ? 1 : 0;
    }

    public boolean eat(int now_location, int target_location ) {
        Chess attacker = board[now_location];
        Chess target = board[target_location];
        boolean canEat = false;

        if (attacker.weight >= target.weight) canEat = true;
        else if (attacker.weight == 1 && target.weight == 7) canEat = true;
        else if (attacker.weight == 7 && target.weight == 1) canEat = false;
        else canEat = false;

        if (attacker.name.equals("砲") || attacker.name.equals("炮")) {
            int dist = Math.abs(now_location / 8 - target_location / 8) + Math.abs(now_location % 8 - target_location % 8);

            if (board[target_location].name.equals("＿")) {
                if (dist == 1) {
                    return move(now_location, target_location);
                }
                else {
                    System.out.println("只能移動至相鄰空格位置！");
                    return false;
                }
            }
            else {
                int countChess = countBetweenChess(now_location, target_location);

                if (target.isOpened) {
                    if (countChess == 1 && attacker.side != target.side) {
                        return move(now_location, target_location);
                    } else {
                        System.out.println("無法吃我方棋子！");
                        return false;
                    }
                }
                else {
                    System.out.println("無法吃蓋著的棋子！");
                    return false;
                }
            }
        }
        else if (canEat) {
            return move(now_location, target_location);
        }
        else {
            System.out.println("無法吃掉對方棋子");
            return false;
        }
    }

    // 確認欲吃掉的棋子是否為我方
    public boolean checkSameSide(int origin_loc, int target_loc) {

        if (board[target_loc].name.equals("＿")) {
            return true;
        }

        if (board[origin_loc].side == board[target_loc].side) { // 無效目的位置
            System.out.println("無法吃掉我方棋子！");
            return false;
        }
        else return true; // 有效目的位置，可移動
    }

    // 將輸入的目標移動位置轉成整數型態
    public int caltarget(String location) {
        int row = location.charAt(0) - 'A';
        int col = location.charAt(1) - '1';
        int index = row * 8 + col;

        return index;
    }

    public String colorSide() {
        String color = " ";
        if (Player1_side == 0) color = "(紅方)";
        else if (Player1_side == 1) color = "(黑方)";
        else color = " ";

        return color;
    }
}

