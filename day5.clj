(ns day5
  (:require [clojure.string :as s])
  (:import (java.lang Integer)))

(def example-input (slurp "day5input_eg.txt"))

(defn points-between-1d [a b]
  (if (> a b)
    (reverse (range b (inc a)))
    (range a (inc b))))

(comment
  (points-between-1d 3 6)
  (points-between-1d 6 3))

(defn points-between [[x1 y1] [x2 y2]]
  (if (= x1 x2)
    (map (fn [y] [x1 y]) (points-between-1d y1 y2))
    (map (fn [x] [x y1]) (points-between-1d x1 x2))))

(comment
  (points-between [5 4] [5 2]))

(defn add-points [m points]
  (reduce (fn [acc point]
            (if (acc point)
              (update acc point inc)
              (assoc acc point 1)))
          m
          points))

(comment
  (add-points
    (add-points {} [[1 2] [1 2] [2 3]])
    [[1 2] [2 3] [3 4]]))

(defn part1 [input]
  (->> (s/split input #"\n")
       (map (fn [line]
              (map (fn [s-inner]
                     (map (fn [s-num]
                            (Integer/parseInt s-num))
                          (s/split s-inner #",")))
                   (s/split line #"\ ->\ "))))
       (filter (fn [[[x1 y1] [x2 y2]]]
                 (or (= x1 x2)
                     (= y1 y2))))
       (reduce (fn [point-count [a b]]
                 (add-points point-count (points-between a b)))
               {})
       (filter (fn [[_ v]] (> v 1)))
       count))

(comment
  (part1 example-input)
  (part1 (slurp "day5input.txt")))

;; part2
(defn points-between-diag [[x1 y1] [x2 y2]]
  (map vector
       (points-between-1d x1 x2)
       (points-between-1d y1 y2)))

(defn points-between2 [[x1 y1] [x2 y2]]
  (cond
    (= x1 x2)
    (map (fn [y] [x1 y]) (points-between-1d y1 y2))
    (= y1 y2)
    (map (fn [x] [x y1]) (points-between-1d x1 x2))
    :else
    (points-between-diag [x1 y1] [x2 y2])))

(comment
  (points-between2 [0 2] [2 2])
  (points-between2 [2 0] [2 2])
  (points-between2 [0 0] [2 2])
  (points-between2 [2 0] [0 2])
  (points-between2 [0 2] [2 0])
  (points-between2 [2 2] [0 0]))

(defn part2 [input]
  (->> (s/split input #"\n")
       (map (fn [line]
              (map (fn [s-inner]
                     (map (fn [s-num]
                            (Integer/parseInt s-num))
                          (s/split s-inner #",")))
                   (s/split line #"\ ->\ "))))
       (reduce (fn [point-count [a b]]
                 (add-points point-count (points-between2 a b)))
               {})
       (filter (fn [[_ v]] (> v 1)))
       count))

(comment
  (part2 example-input)
  (part2 (slurp "day5input.txt")))
