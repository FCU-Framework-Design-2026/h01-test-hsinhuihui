package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        game.generateChess();
        game.setPlayers("小A", "小B");

        Scanner input = new Scanner(System.in);

        while (!game.gameOver()) {
            int quit = 0;
            System.out.println();
            game.showAllChess();
            String currentPlayer = (game.turn == 0) ? "小A" : "小B";
            System.out.println("現在輪到: " + currentPlayer + " " + game.colorSide());
            System.out.print("請輸入欲翻開/移動之棋子位置(如：A1): ");

            String str_location = input.next().toUpperCase(); // 輸入位置
            int target_location = game.caltarget(str_location); // 取得整數型態的位置

            if (target_location < 0 || target_location > 31) {
                System.out.println("請輸入正確位置！");
                continue;
            }

            if (game.board[target_location].isOpened) { // 選擇已翻開棋子進行移動

                if (game.Player1_side != -1 && game.board[target_location].side != game.Player1_side) {
                    System.out.println("此棋子並非你的，請重新選擇！");
                    continue;
                }

                System.out.println("選擇之棋子為: " + game.board[target_location].name);

                while (true) {
                    int now_location = target_location;
                    quit = 0;

                    System.out.print("請輸入目標移動位置(或輸入 Q 取消移動/吃子，改為翻開棋子): ");
                    String sec_str_location = input.next().toUpperCase();

                    if (sec_str_location.equals("Q")) {
                        quit = 1;
                        break;
                    }
                    else quit = 0;

                    int sec_target_location = game.caltarget(sec_str_location);

                    if (sec_target_location < 0 || sec_target_location > 31) {
                        System.out.println("請輸入正確位置！");
                        continue;
                    }

                    int now_row = now_location / 8, now_col = now_location % 8;
                    int target_row = sec_target_location / 8, target_col = sec_target_location % 8;
                    int dist = Math.abs(now_row - target_row) + Math.abs(now_col - target_col);

                    if (game.board[now_location].name.equals("砲") || game.board[now_location].name.equals("炮")) {
                        if (dist == 1 && game.board[sec_target_location].side != -1) {
                            System.out.println("砲/炮必須隔一個棋子才能吃子或只能移動至相鄰空格位置！");
                            continue;
                        }
                        else if (dist != 1 && game.countBetweenChess(now_location, sec_target_location) != 1) {
                            System.out.println("砲/炮只能移動到相鄰空格位置或在上/下/左/右恰好隔一個棋子才能吃子！");
                            continue;
                        }
                        else if (game.eat(now_location, sec_target_location)) break;
                        else continue;
                    }

                    if (dist != 1) {
                        System.out.println("只能移動到相鄰空格位置！");
                        continue;
                    }

                    if (!game.board[sec_target_location].isOpened) {
                        System.out.println("無法吃掉未翻開的棋子！");
                        continue;
                    }

                    if (game.checkSameSide(now_location, sec_target_location)) { // 代表不同陣營，吃對方棋子
                        if (game.eat(now_location, sec_target_location)) break;
                        else continue;
                    }
                }
                if (quit == 1) continue;
                game.Player1_side = (game.Player1_side == 0) ? 1 : 0;
                game.nextTurn();
            }
            else {
                if (game.Player1_side == -1) { // 遊戲開始第一個人翻牌
                    game.Player1_side = game.board[target_location].side;
                    System.out.println("玩家 小A 為: " + game.colorSide());
                }

                game.board[target_location].isOpened = true; // 翻開棋子
                System.out.println(game.colorSide() + " 翻開了: " + game.board[target_location].name);
                game.Player1_side = (game.Player1_side == 0) ? 1 : 0;
                game.nextTurn();
            }
        }
    }
}
