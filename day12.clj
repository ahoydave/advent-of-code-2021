(ns day12
  (:require [clojure.string :as s]))

(def example-input (slurp "day12input_eg.txt"))

(defn edge-lookup [input]
  (reduce (fn [lookup edge]
            (if (lookup (first edge))
              (update lookup (first edge) conj (second edge))
              (assoc lookup (first edge) [(second edge)])))
          {}
          (mapcat (fn [line]
                    [(s/split line #"-")
                     (reverse (s/split line #"-"))])
                  (s/split input #"\n"))))

(defn extend-path [lookup path]
  (if (= "end" (last path))
    [path]
    (map (fn [new-step]
          (conj path new-step))
        (lookup (last path)))))

(defn extend-paths [lookup paths]
  (mapcat (partial extend-path lookup) paths))

(defn is-lower? [x]
  (= (s/lower-case x) x))

(comment
  (is-lower? "A"))

(defn repeated-small-cave [path]
  (->> path
       frequencies
       (filter #(and (> (val %) 1)
                     (is-lower? (key %))))
       count
       pos?))

(comment
  (repeated-small-cave ["a" "B" "aa"]))

(defn remove-invalid-paths [paths]
  (->> paths
       (remove repeated-small-cave)
       distinct))

(defn all-ended? [paths]
  (= 0 (->> paths
            (filter #(not= "end" (last %)))
            count)))

(comment
  (remove-invalid-paths [["a" "a"] ["a" "b"] ["a" "B" "B"] ["a" "B" "B"]])
  (all-ended? [["a" "end"] ["B" "end"]])
  (all-ended? [["a" "end"] ["B" "a"]]))

(defn part1 [input]
  (count
    (let [lookup (edge-lookup input)]
      (loop [paths [["start"]]]
        (if (all-ended? paths)
          paths
          (recur (->> paths
                      (extend-paths lookup)
                      remove-invalid-paths)))))))

(comment
  (part1 example-input)
  (part1 (slurp "day12input_eg2.txt"))
  (part1 (slurp "day12input_eg3.txt"))
  (part1 (slurp "day12input.txt")))

;;part 2

(defn repeated-small-cave2 [path]
  (or
    (->> path
         frequencies
         (filter #(and (> (val %) 1)
                       (is-lower? (key %))))
         count
         (#(> % 1)))
    (->> path
        frequencies
        (filter #(and (> (val %) 2)
                      (is-lower? (key %))))
        count
        pos?)))

(comment
  (repeated-small-cave2 ["a" "B" "aa" "a" "a"])
  (repeated-small-cave2 ["a" "B" "aa" "a" "b"])
  (repeated-small-cave2 ["a" "B" "aa" "a" "aa"]))

(defn repeated-cave [path cave]
  (->> path
       (filter #(= % cave))
       count
       (#(> % 1))))

(defn repeated-start-end [path]
  (or (repeated-cave path "start")
      (repeated-cave path "end")))

(defn remove-invalid-paths2 [paths]
  (->> paths
       (remove repeated-small-cave2)
       (remove repeated-start-end)
       distinct))

(defn part2 [input]
  (count
    (let [lookup (edge-lookup input)]
      (loop [paths [["start"]]]
        (if (all-ended? paths)
          paths
          (recur (->> paths
                      (extend-paths lookup)
                      remove-invalid-paths2)))))))

(comment
  (part2 example-input)
  (part2 (slurp "day12input_eg2.txt"))
  (part2 (slurp "day12input_eg3.txt"))
  (part2 (slurp "day12input.txt")))

