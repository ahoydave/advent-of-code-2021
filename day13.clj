(ns day13
  (:require [clojure.string :as s])
  (:import (java.lang Integer)))

(def example-input (slurp "day13input_eg.txt"))

(defn read-points [input]
  (map (fn [line]
         (let [[x y] (s/split line #",")]
           [(Integer/parseInt x) (Integer/parseInt y)]))
       (s/split input #"\n")))

(defn read-folds [input]
  (map (fn [line]
         (let [[fold-type v] (s/split (subs line 11) #"=")]
           [fold-type (Integer/parseInt v)]))
       (s/split input #"\n")))

(defn do-fold [points [fold-type fold-val]]
  (if (= fold-type "y")
    (->> points
         (map (fn [[x y]]
                (if (> y fold-val)
                  [x (- (* 2 fold-val) y)]
                  [x y])))
         distinct)
    (->> points
         (map (fn [[x y]]
                (if (> x fold-val)
                  [(- (* 2 fold-val) x) y]
                  [x y])))
         distinct)))

(defn part1 [input]
  (let [[points-str folds-str] (s/split input #"\n\n")
        points                 (read-points points-str)
        folds                  (read-folds folds-str)]
    (count (do-fold points (first folds)))))

(comment
  (part1 example-input)
  (part1 (slurp "day13input.txt")))

;;part2
(defn fold-part2 [input]
  (let [[points-str folds-str] (s/split input #"\n\n")
        points                 (read-points points-str)
        folds                  (read-folds folds-str)]
    (reduce #(do-fold %1 %2)
            points
            folds)))

(defn part2 [input]
  (let [folded (set (fold-part2 input))
        [maxx maxy]
        (reduce
          (fn [[maxx maxy] [x y]]
            [(max maxx x) (max maxy y)])
          [0 0]
          folded)]
    (doseq [y (range (inc maxy))]
      (doseq [x (range (inc maxx))]
        (if (folded [x y])
          (print "#")
          (print ".")))
      (print "\n"))))

(comment
  (part2 example-input)
  (part2 (slurp "day13input.txt")))








