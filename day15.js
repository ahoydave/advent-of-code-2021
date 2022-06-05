const fs = require('fs');

// const file = fs.readFileSync('day15input_eg.txt', { encoding: 'utf8' });
const file = fs.readFileSync('day15input.txt', { encoding: 'utf8' });
const pieces = file.split('\n');

function riskAt(x, y) {
    return Number(pieces.at(y).at(x));
}

function getAdjacent(x, y) {
    return [[x + 1, y], [x - 1, y], [x, y + 1], [x, y - 1]]
        .filter(([x, y]) => x >= 0 && y >= 0 && x < pieces[0].length && y < pieces.length)
}

function posToKey(x, y) {
    return `${x},${y}`
}

const paths = new Map();
paths.set(posToKey(0, 0), 0);
const processStack = [[0, 0]];

while (processStack.length > 0) {
    const [currX, currY] = processStack.pop();
    const currRisk = paths.get(posToKey(currX, currY));
    for ([x, y] of getAdjacent(currX, currY)) {
        if ((paths.get(posToKey(x, y)) ?? Number.MAX_VALUE) > currRisk + riskAt(x, y)) {
            paths.set(posToKey(x, y), currRisk + riskAt(x, y));
            processStack.push([x, y]);
        }
    }
}

// console.log(paths);
// console.log(processStack);
console.log(paths.get(posToKey(pieces[0].length - 1, pieces.length - 1)));