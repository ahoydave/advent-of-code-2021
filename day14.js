const fs = require('fs');

// const input = fs.readFileSync('day14input_eg.txt', { encoding: 'UTF8' });
const input = fs.readFileSync('day14input.txt', { encoding: 'UTF8' });

const pieces = input.split('\n');
let seed = pieces[0];
const rules = pieces.slice(2);

const ruleLookup = new Map();

for (s of rules) {
    const [key, value] = s.split(' -> ');
    ruleLookup.set(key, value);
}

function countAdd(counter, k, v) {
    counter.set(k, (counter.get(k) ?? 0) + v);
}

const chars = new Map();
let pairs = new Map();
for (let i = 0; i < seed.length - 1; i++) {
    countAdd(chars, seed.at(i), 1);
    countAdd(pairs, seed.at(i) + seed.at(i+1), 1);
}
countAdd(chars, seed.at(-1), 1);

//set to 10 for part 1
const rewrites = 40;
for (let i = 0; i < rewrites; i++) {
    const updatedPairs = new Map();
    for (const [pair, count] of pairs.entries()) {
        const newChar = ruleLookup.get(pair);
        countAdd(chars, newChar, count);
        countAdd(updatedPairs, pair.at(0) + newChar, count);
        countAdd(updatedPairs, newChar + pair.at(1), count);
    }
    pairs = updatedPairs;
}

const countVals = Array.from(chars.values());
countVals.sort((a, b) => a - b);

console.log(countVals.at(-1) - countVals.at(0));