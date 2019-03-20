import csv
import matplotlib.pyplot as plt
import os


def mean(numbers):
    return float(sum(numbers)) / max(len(numbers), 1)


def get_file_names(dir):
    return os.listdir(dir)


def read_file(dir, file):
    with open(dir + '/' + file) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=';')

        idxs = []
        elapsed_time = []
        lengths = []
        costs = []

        pred_hyper = []
        pred_poly = []
        pred_linear = []

        line_count = 0
        for row in csv_reader:
            if line_count == 0:
                line_count += 1
                continue

            idxs.append(int(row[0]))
            elapsed_time.append(int(row[1]))
            lengths.append(int(row[2]))
            costs.append(float(row[3]))

            pred_hyper.append(float(row[4]))
            pred_poly.append(float(row[5]))
            pred_linear.append(float(row[6]))

            line_count += 1

    return {'idx': idxs, 'elapsed': elapsed_time, 'iter_len': lengths, 'costs': costs, 'pred_hyper': pred_hyper, 'pred_poly': pred_poly, 'pred_linear': pred_linear}


def go(input_dir, output_dir, save):
    iteration_length = []
    for file in get_file_names(input_dir):
        data = read_file(input_dir, file)

        iteration_length.extend(data['iter_len'])

        plt.title(file)

        plt.plot(data['costs'], label='cost')
        plt.plot(data['pred_hyper'], label='pred_hyper')
        plt.plot(data['pred_poly'], label='pred_poly')
        plt.plot(data['pred_linear'], label='pred_linear')

        plt.gca().legend(('cost', 'hyper', 'poly', 'linear'))

        if save:
            plt.savefig(output_dir + '/' + file + '.png')
        plt.show()

    plt.figure(dpi=680)
    plt.title("Mean: " + str(mean(iteration_length)))
    pts = plt.scatter(range(0, len(iteration_length)), iteration_length, marker='o', c='b', s=1)
    plt.setp(pts, color='r', linewidth=0.1)
    if save:
        plt.savefig(output_dir + 'iteration_len.png')
    plt.show()


if __name__ == '__main__':
    go("/home/lukas/repos/bp/job-data/fitted-out/input", "/home/lukas/repos/bp/job-data/fitted-out/graphs", True)
