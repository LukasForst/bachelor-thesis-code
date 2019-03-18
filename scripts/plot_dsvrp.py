import csv
import os

import matplotlib.pyplot as plt


def mean(numbers):
    return float(sum(numbers)) / max(len(numbers), 1)


def get_file_names(dir):
    return os.listdir(dir)


def read_file(dir, file):
    with open(dir + file) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=';')

        idxs = []
        elapsed_time = []
        lengths = []
        costs = []

        line_count = 0
        for row in csv_reader:
            if line_count == 0:
                line_count += 1
                continue

            idxs.append(int(row[0]))
            elapsed_time.append(int(row[1]))
            lengths.append(int(row[2]))
            costs.append(float(row[3]))
            line_count += 1

    return {'idx': idxs, 'elapsed': elapsed_time, 'iter_len': lengths, 'costs': costs}


def go(input_dir, output_dir):
    iteration_length = []
    for file in get_file_names(input_dir):
        data = read_file(input_dir, file)

        iteration_length.extend(data['iter_len'])

        plt.title(file)
        plt.plot(data['idx'], data['costs'])
        plt.savefig(output_dir + file + '.png')
        plt.show()

    plt.figure(dpi=680)
    plt.title("Mean: " + str(mean(iteration_length)))
    pts = plt.scatter(range(0, len(iteration_length)), iteration_length, marker='o', c='b', s=1)
    plt.setp(pts, color='r', linewidth=0.1)
    plt.savefig(output_dir + 'iteration_len.png')


if __name__ == '__main__':
    go("/home/lukas/repos/bp/job-data/input/", "/home/lukas/repos/bp/job-data/graphs/")
