var fs = require('fs');
const assert = require('node:assert').strict;

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

function expandAndCount(left, right, rewrites, acc) {
    console.log(`current depth is ${rewrites}`);
    if (rewrites === 0) {
        // console.log(`adding ${left}`);
        acc.set(left, (acc.get(left) ?? 0) + 1);
    } else {
        const middle = ruleLookup.get(left + right);
        expandAndCount(left, middle, rewrites - 1, acc);
        expandAndCount(middle, right, rewrites - 1, acc);
    }
}

const counts = new Map();
const depth = 40;
for (let i = 0; i < seed.length - 1; i++) {
    console.log(`Expanding at index ${i}`);
    expandAndCount(seed[i], seed[i+1], depth, counts);
}
// console.log(`adding ${seed.at(-1)}`);
counts.set(seed.at(-1), (counts.get(seed.at(-1)) ?? 0) + 1);
console.log(counts);

const countVals = Array.from(counts.values());
countVals.sort((a, b) => a - b);

console.log(countVals.at(-1) - countVals.at(0));