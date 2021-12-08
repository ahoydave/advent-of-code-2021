(ns day7
  (:require [clojure.edn :as edn])
  (:import (java.lang Math)))

(def example-input [16,1,2,0,4,2,7,1,2,14])

(defn cost-to-align [init-positions target]
  (reduce (fn [acc position]
            (+ acc (Math/abs (- position target))))
          0
          init-positions))

(comment
  ;; Fairly simple to prove it must lie between min/max pos
  ;; Brute force search
  (->> (range (reduce min example-input)
              (inc (reduce max example-input)))
       (map (fn [pos] [pos (cost-to-align example-input pos)]))))

;; We can improve the cost calc if we use frequencies
(defn cost-to-align2 [freqs target]
  (reduce (fn [acc [position count]]
            (+ acc (* count (Math/abs (- position target)))))
          0
          freqs))

(comment
  (= (cost-to-align example-input 2)
     (cost-to-align2 (frequencies example-input) 2)))

(defn cost-for-pos [position]
  (cost-to-align2 (frequencies example-input) position))

(defn best-position-cost [cost-to-align-fn positions]
  (let [cost-for-pos (fn [position]
                       (cost-to-align-fn (frequencies positions) position))]
    (reduce (fn [[best-pos best-cost] new-pos]
              (let [new-cost (cost-for-pos new-pos)]
                (if (< new-cost best-cost)
                  [new-pos new-cost]
                  [best-pos best-cost])))
            [(first positions) (cost-for-pos (first positions))]
            (range (apply min positions)
                   (inc (apply max positions))))))

(comment
  (best-position-cost cost-to-align2 example-input)

  (->> (slurp "day7input.txt")
       (format "[%s]")
       edn/read-string
       (best-position-cost cost-to-align2)))

(defn step-increase [n]
  (/ (* n (+ 1 n)) 2))

(comment
  (->> [1 2 3 4 5]
       (map step-increase)))

;; for part 2 - a different cost fn
(defn cost-to-align3 [freqs target]
  (reduce (fn [acc [position count]]
            (+ acc (* count 
                      (step-increase (Math/abs (- position target))))))
          0
          freqs))

(comment
  (best-position-cost cost-to-align3 example-input)

  (->> (slurp "day7input.txt")
       (format "[%s]")
       edn/read-string
       (best-position-cost cost-to-align3)))
