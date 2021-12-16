(ns day11
  (:require [clojure.string :as s]))

(def example-input (slurp "day11input_eg.txt"))

(defn input->grid [input]
  (vec
    (map
      (fn [line]
        (vec
          (map #(- (int %) (int \0))
               line)))
      (s/split input #"\n"))))

(defn inc-grid [grid]
  (vec (map (fn [row]
              (vec (map inc row)))
            grid)))

(defn grid-val [x y grid]
  (nth (nth grid y) x))

(defn find-flashes [grid]
  (for [x (range (count (first grid)))
        y (range (count grid))
        :when (= 10 (grid-val x y grid))]
    [x y]))

(defn inc-at-pos [x y grid]
  (if (and (>= x 0) (>= y 0) (< x (count (first grid))) (< y (count grid))
           (< (grid-val x y grid) 10))
    [(= (grid-val x y grid) 9)
     (update grid y
                  #(update % x inc))]
    [false grid]))

(defn points-around [x y]
  [[(dec x) (dec y)] [(dec x) y] [(dec x) (inc y)]
   [x (dec y)] [x (inc y)]
   [(inc x) (dec y)] [(inc x) y] [(inc x) (inc y)]])

(comment
  (points-around 0 0))

(defn grow-flash [[x y] grid]
  (reduce (fn [[latest-grid new-flashes] [this-x this-y]]
            (let [[updated? new-grid] (inc-at-pos this-x this-y latest-grid)]
              [new-grid (if updated? (conj new-flashes [this-x this-y]) new-flashes)]))
          [grid []]
          (points-around x y)))

(defn process-flashes [grid]
  (loop [current-grid    grid
         flashes (find-flashes grid)]
    (if (seq flashes)
      (let [[updated-grid new-flashes] (grow-flash (first flashes) current-grid)]
        (recur updated-grid (concat new-flashes (rest flashes))))
      current-grid)))

(defn count-flashes [grid]
  (->> grid
       (map (fn [row]
              (->> row
                   (filter #(= 10 %))
                   count)))
       (reduce +)))

(defn reset-flashes [grid]
  (vec
    (map (fn [row]
          (vec (map #(if (= 10 %) 0 %) row)))
         grid)))

(defn part1 [input]
  (let [grid  (input->grid input)]
    (first
      (reduce (fn [[flash-count current-grid] _]
               (let [flashed-grid  (-> current-grid
                                      inc-grid
                                      process-flashes)
                     num-new-flash (count-flashes flashed-grid)
                     updated-grid  (reset-flashes flashed-grid)]
                 [(+ flash-count num-new-flash) updated-grid]))
             [0 grid]
             (range 100)))))

(comment
  (part1 example-input)
  (part1 (slurp "day11input.txt")))

(defn part2 [input]
  (let [grid (input->grid input)]
    (loop [current-grid grid
           step-count 1]
      (let [flashed-grid  (-> current-grid
                              inc-grid
                              process-flashes)
            num-new-flash (count-flashes flashed-grid)
            updated-grid  (reset-flashes flashed-grid)]
        (if (= 100 num-new-flash)
          step-count
          (recur updated-grid (inc step-count)))))))

(comment
  (part2 example-input)
  (part2 (slurp "day11input.txt")))






























