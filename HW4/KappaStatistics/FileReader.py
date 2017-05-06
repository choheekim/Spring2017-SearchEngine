import os

CURRENT_FILE_PATH = os.getcwd()

'''
RelevanceFilesReader class read files from a directory that contains peers' relevance judgement files
and my relevance judgement file and computes k value.
'''
class RelevanceFilesReader:

    def __init__(self):
        self.dic_file_path = CURRENT_FILE_PATH + "/relevanceJudgementFiles" #file path for the directory that contains relevance judgement files of peers
        self.my_file_path = CURRENT_FILE_PATH + "/kim-qrels.txt"    #file path for my relevance judgment file
        self.file_list = [] #it will contains list of files

    '''
    get_file_list iterates file in the directory.
    '''
    def get_file_list(self):
        for file_name in os.listdir(self.dic_file_path):
            file_dict = self.get_my_file_dic(self.dic_file_path + "/" + file_name)
            self.file_list.append(file_dict)

    '''
    get_my_file_dic opens file, read line by line, and stores necessary information - query id, document id, relevancy -
    to the given data structure.
    '''
    def get_my_file_dic(self, file_name):

        with open(file_name, "r", encoding='utf-8') as f:

            file_dict = dict()
            contents = f.read().splitlines()

            for content in contents:
                content_list = str(content).split(" ")
                query_id = int(content_list[0])
                doc_id = int(content_list[2])
                relevancy = int(content_list[3])

                if not file_dict.__contains__(query_id):
                    relevancy_list = []
                else:
                    relevancy_list = file_dict.get(query_id)

                relevancy_list.append(RelevancyList(doc_id, relevancy))
                file_dict[query_id] = relevancy_list

        return file_dict

    '''
    get_kappa_coefficient computes k for two files - my file and current relevence judgement file in file list.
    '''
    def get_kappa_coefficient(self):
        self.get_file_list()
        my_file_dic = self.get_my_file_dic(self.my_file_path)
        k_list = list()

        for rj_file in self.file_list:
            p11 = 0
            p12 = 0
            p21 = 0
            p22 = 0

            # iterate from 1 to 4 since the query id is from 1 to 3.
            for i in range(1, 4):
                cur_query_id = i

                my_file_relevancy_list = my_file_dic[cur_query_id]
                compare_file_relevancy = rj_file[cur_query_id]

                # each list in dictionary contains 10 element because we have 10 documents. Iterate each
                for idx in range(0, 10):
                    my_file_boolean = my_file_relevancy_list[idx].get_relevancy()
                    compare_file_boolean = compare_file_relevancy[idx].get_relevancy()

                    if my_file_boolean and compare_file_boolean:
                        p11 += 1
                    elif not my_file_boolean and not compare_file_boolean:
                        p22 += 1
                    else:
                        if my_file_boolean:
                            p21 += 1
                        else:
                            p12 += 1
            p_o = p11 + p22
            p_e = (p11 + p12)*(p11 + p12) + (p21 + p22)*(p21 + p22)
            k = (p_o - p_e)/(1-p_e)

            k_list.append(k)
            self.write_file(k_list)

    def write_file(self, k_list):
        file = open("kappa_statistics_file.txt", "w")
        for k in k_list:
            file.write(str(k) + "\n")


'''
RelevancyList class holds data - document id and relevancy.
'''
class RelevancyList:

    def __init__(self, doc_id, relevancy):
        self.doc_id = doc_id
        self.relevancy = relevancy

    def get_document_id(self):
        return self.doc_id

    def get_relevancy(self):
        return self.relevancy


