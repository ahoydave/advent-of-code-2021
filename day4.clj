(ns day4
  (:require [clojure.string :as s]
            [clojure.set :as set])
  (:import (java.lang Integer)))


(def example-input (slurp "day4input_eg.txt"))

(defn numbers [input]
  (-> input
      (s/split #"\n")
      first
      (s/split #",")))

(defn rows [board]
  (->> board
       (map s/trim)
       (map #(s/split % #"\s+"))))

(defn cols [board]
  (->> board
       rows
       (apply interleave)
       (partition 5)))

(defn win-sets [board]
  (map set (concat (rows board) (cols board))))

(defn board-set [board]
  (set (apply concat (rows board))))

(defn prepare-board [board]
  [(win-sets board) (board-set board)])

(defn board-wins? [board num-set]
  (->> (first board)
       (some (fn [win-set]
               (set/subset? win-set num-set)))))

(defn board-score [board num-set]
  (->> (set/difference (second board) num-set)
       (map #(Integer/parseInt %))
      (reduce +)))

(defn part1 [input]
  (let [nums (numbers input)
        boards (->> (-> input
                        (s/split #"\n")
                        rest)
                    (partition 6)
                    (map rest)
                    (map prepare-board))]
    (loop [uncalled-nums nums
           curr-nums     []]
     (let [curr-num-set (set curr-nums)
           winners      (filter #(board-wins? % curr-num-set) boards)]
       (if (pos? (count winners))
         (* (Integer/parseInt (last curr-nums)) (board-score (first winners) curr-nums))
         (recur (rest uncalled-nums) (conj curr-nums (first uncalled-nums))))))))

(comment
  (part1 example-input)
  (part1 (slurp "day4input.txt")))
