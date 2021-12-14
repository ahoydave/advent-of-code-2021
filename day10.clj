(ns day10
  (:require [clojure.string :as s]))

(def example-input (slurp "day10input_eg.txt"))

(def pairs
  {\( \)
   \[ \]
   \{ \}
   \< \>})

(def openers (set (keys pairs)))

(def scores
  {\) 3
   \] 57
   \} 1197
   \> 25137})

(defn process-line [line]
  (reduce (fn [stack item]
            (cond
              (= (first stack) :corrupted)
              stack

              (contains? openers item)
              (conj stack item)

              :else
              (if (= item
                     (pairs (first stack)))
                (rest stack)
                [:corrupted (scores item)])))
          '()
          line))

(defn part1 [input]
  (->> (s/split input #"\n")
       (map process-line)
       (filter #(= :corrupted (first %)))
       (map second)
       (reduce +)))

(comment
  (part1 example-input)
  (part1 (slurp "day10input.txt")))

;; part2
(def scores2
  {\( 1
   \[ 2
   \{ 3
   \< 4})

(defn score-stack [stack]
  (reduce (fn [score item]
            (+ (scores2 item)
               (* 5 score)))
          0
          stack))

(defn part2 [input]
  (->> (s/split input #"\n")
       (map process-line)
       (filter #(not= :corrupted (first %)))
       (map score-stack)
       sort
       (#(nth % (/ (count %) 2)))))

(comment
  (part2 example-input)
  (part2 (slurp "day10input.txt")))
