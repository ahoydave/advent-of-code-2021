var fs = require('fs');
const assert = require('node:assert').strict;

const input = fs.readFileSync('day14input_eg.txt', { encoding: 'UTF8' });
// const input = fs.readFileSync('day14input.txt', { encoding: 'UTF8' });

const pieces = input.split('\n');
let seed = pieces[0];
const rules = pieces.slice(2);

const ruleLookup = new Map();

for (s of rules) {
    const [key, value] = s.split(' -> ');
    ruleLookup.set(key, value);
}

for (let rewriteCount = 0; rewriteCount < 40; rewriteCount++) {
    let updated = seed[0];
    for (let i = 0; i < seed.length - 1; i++) {
        updated += ruleLookup.get(seed.substring(i, i + 2)) + seed[i+1];
    }
    seed = updated;
}

const counts = Array.from(seed).reduce((acc, val) => {
    acc.set(val, (acc.get(val) ?? 0) + 1);
    return acc;
}, new Map());

const countVals = Array.from(counts.values());
countVals.sort((a, b) => a - b);

console.log(countVals.at(-1) - countVals.at(0));