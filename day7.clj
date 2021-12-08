(ns day7
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

(defn best-position-cost [positions]
  (let [cost-for-pos (fn [position]
                       (cost-to-align2 (frequencies example-input) position))]
    (reduce (fn [[best-pos best-cost] new-pos]
              (let [new-cost (cost-for-pos new-pos)]
                (if (< new-cost best-cost)
                  [new-pos new-cost]
                  [best-pos best-cost])))
            [(first example-input) (cost-for-pos (first example-input))]
            (range (apply min example-input)
                   (inc (apply max example-input))))))

(comment
  (best-position-cost example-input))
