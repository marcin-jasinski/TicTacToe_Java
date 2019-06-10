debugger;

var maxDepth = 5;

var RESULT = {
    gameInProgress: 0,
    playerXWon: 'X',
    playerOWon: 'O',
    tie: 3
};

function moveCounter(board){
    var moveCounter = 0;
    for (var i = 0; i<board.length; i++){
        if (board[i] != " "){
            moveCounter++
        }
    }
    return moveCounter;
}

function getGameResult(board,symbol){

    var result = RESULT.gameInProgress;
    if (moveCounter(board)<5){
        return result;
    }

    var positionsToCheck = [
        [0,1,2],[1,2,3],[4,5,6],[5,6,7],[8,9,10],[9,10,11],[12,13,14],[13,14,15],
        [0,4,8],[4,8,12],[1,5,9],[5,9,13],[2,6,10],[6,10,14],[3,7,11],[7,11,15],
        [1,6,11],[0,5,10],[5,10,15],[4,9,14],
        [2,5,8],[3,6,9],[6,9,12],[7,10,13]
    ];

    function checkRowColDiag(positions) {
        var rowColDiag = [board[positions[0]],board[positions[1]],board[positions[2]]];
        line = rowColDiag.join('');
        return line;
    }

    function threeCharsInRow (line){
        return line[0] == symbol && line[1] == symbol && line[2] == symbol;
    }

    for(var i = 0; i < positionsToCheck.length; i++){
        var positions = positionsToCheck[i];
        var line = checkRowColDiag(positions);
        if(threeCharsInRow(line)){
            result = symbol;
            return result;
        }
    }

    if (moveCounter(board) == 16){
        result=RESULT.tie;
        return result;
    }

    return result;
}

function getBestMove (board,symbol,currentDepth){

    function copyBoard(board) {
        var copy = [];
        for (var i = 0 ; i < board.length ; i++){
            copy.push(board[i]);
        }
        return copy
    }

    function getAvailableMoves(board) {
        var availableMoves = [];
        for (var i = 0 ; i < board.length ; i++){
            if (board[i] == " "){
                availableMoves.push(i);
            }
        }

        return availableMoves
    }

    var availableMoves = getAvailableMoves(board)
    var availableMovesAndScores = []

    for (var i=0 ; i<availableMoves.length ; i++){
        var move = availableMoves[i]
        var newBoard = copyBoard(board)
        // var newBoard = board.slice();
        newBoard[move] = symbol;
        var result = getGameResult(newBoard,symbol);
        var score
        if (result == RESULT.tie) {score = 0}
        else if (result == symbol) {
            score = 1
        }
        else {
            if(currentDepth != maxDepth){
                var otherSymbol = (symbol=='X')? 'O' : 'X';
                var nextMove = getBestMove(newBoard, otherSymbol,currentDepth+1);
                score = -(nextMove.score)
            }
        }
        if(score === 1) return ({move: move, score: score});
        availableMovesAndScores.push({move: move, score: score})
    }

    function compare( a, b ) {
        if ( a.score < b.score ){
            return -1;
        }
        if ( a.score > b.score ){
            return 1;
        }
        return 0;
    }

    availableMovesAndScores.sort(compare)

    return availableMovesAndScores[0]
}

function makeNextMove(gameBoard, symbol) {

    var move = getBestMove(gameBoard,symbol,1).move;
    gameBoard[move] = symbol;
    return gameBoard;
}