#!/usr/bin/env python

import copy
import time

def create_board():
    board = []
    for i in range(rows):
        row = []
        for j in range(columns):
            row.append(0)
        board.append(row)
    return board

def init_board(board):
    # Glider
    # rows & cols - at least 4x4
    board[0][2] = 1
    board[1][0] = 1
    board[1][2] = 1
    board[2][1] = 1
    board[2][2] = 1

def print_board(board):
    for i in range(columns):
        print('._', end='')
    print('.')

    for i in range(rows):
        for j in range(columns):
            if board[i][j] == 1:
                print('|*', end='')
            else:
                print('|_', end='')
        print('|')

def update_cell(cell):
    i = cell[0]
    j = cell[1]

    nb_cells = 0

    if (j-1) >= 0:
        nb_cells += board[i][j-1]
    if (j+1) < columns:
        nb_cells += board[i][j+1]

    if (i-1) >= 0:
        nb_cells += board[i-1][j]
        if (j-1) >= 0:
            nb_cells += board[i-1][j-1]
        if (j+1) < columns:
            nb_cells += board[i-1][j+1]

    if (i+1) < rows:
        nb_cells += board[i+1][j]
        if (j-1) >= 0:
            nb_cells += board[i+1][j-1]
        if (j+1) < columns:
            nb_cells += board[i+1][j+1]

    if nb_cells < 2 or nb_cells > 3:
        new_board[i][j] = 0
    elif nb_cells == 3:
        new_board[i][j] = 1

def update_cells():
    for i in range(rows):
        for j in range(columns):
            update_cell([i,j])


if __name__ == "__main__":
    rows = 10
    columns = 10
    iter_count = 10
    board = create_board()
    init_board(board)

    for i in range(iter_count):
        print("\033[2J\033[H");
        print_board(board)
        new_board = copy.deepcopy(board)
        update_cells()
        board = new_board
        time.sleep(1)
