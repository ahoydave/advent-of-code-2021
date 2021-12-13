(ns day9
  (:require [clojure.string :as s]
            [clojure.set :as set])
  (:import (java.lang Integer)))

(def example-input (slurp "day9input_eg.txt"))

(defn read-grid [input]
  (map (fn [line]
         (map #(- (int %) (int \0))
              (seq line)))
       (s/split input #"\n")))

(defn grid-pos [x y grid default]
  (let [x-size (count (first grid))
        y-size (count grid)]
    (if (or (< x 0) (< y 0) (>= x x-size) (>= y y-size))
      default
      (nth (nth grid y) x))))

(comment
  (grid-pos -1 1 [[0 1] [2 3]] -1)
  (grid-pos 1 -1 [[0 1] [2 3]] -1)
  (grid-pos 2 1 [[0 1] [2 3]] -1)
  (grid-pos 1 2 [[0 1] [2 3]] -1)
  (grid-pos 1 1 [[0 1] [2 3]] -1)
  (grid-pos 1 0 [[0 1] [2 3]] -1)
  (grid-pos 0 0 [[0 1] [2 3]] -1)
  (grid-pos 0 1 [[0 1] [2 3]] -1))

(defn is-min? [x y grid]
  (let [pos (grid-pos x y grid nil)]
    (and (< pos (grid-pos (dec x) y grid 10))
         (< pos (grid-pos (inc x) y grid 10))
         (< pos (grid-pos x (dec y) grid 10))
         (< pos (grid-pos x (inc y) grid 10)))))

(comment
  (is-min? 0 0 [[0 1] [2 3]])
  (is-min? 1 0 [[0 1] [2 3]])
  (is-min? 0 1 [[0 1] [2 3]])
  (is-min? 1 1 [[0 1] [2 3]]))

(defn part1 [input]
  (let [grid   (read-grid input)
        x-size (count (first grid))
        y-size (count grid)]
    (reduce +
            (for [x     (range x-size)
                  y     (range y-size)
                  :when (is-min? x y grid)]
              (inc (grid-pos x y grid nil))))))

(comment
  (part1 example-input)
  (part1 (slurp "day9input.txt")))

;;part 2

(defn surrounding [x y grid]
  (filter
    (fn [[sx sy]] (not= 9 (grid-pos sx sy grid 9)))
    [[(dec x) y] [(inc x) y] [x (dec y)] [x (inc y)]]))

(comment
  (surrounding 1 0 (read-grid example-input))
  (surrounding 0 0 (read-grid example-input)))

(defn expand-basin [grid points]
  (reduce (fn [acc [x y]]
            (set/union acc (set (surrounding x y grid))))
          points
          points))

(comment
  (expand-basin
    (read-grid example-input)
    (expand-basin (read-grid example-input) #{[0 0]})))

(defn get-basin [x y grid]
  (loop [points #{[x y]}]
    (let [new-points (expand-basin grid points)]
      (if (= new-points points)
        points
        (recur new-points)))))

(comment
  (count (get-basin 2 2 (read-grid example-input))))

(defn part2 [input]
  (let [grid   (read-grid input)
        x-size (count (first grid))
        y-size (count grid)]
    (->> (for [x     (range x-size)
               y     (range y-size)
               :when (is-min? x y grid)]
           (count (get-basin x y grid)))
         sort
         reverse
         (take 3)
         (reduce *))))

(comment
  (part2 example-input)
  (part2 (slurp "day9input.txt")))
