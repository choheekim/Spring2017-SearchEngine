HOW TO RUN:
1. Archive the zip file
2. Run python3 Main.py

FileReader.py:

    1. RelevanceFilesReader:

        Data field:
            dic_file_path #file path for the directory that contains relevance judgement files of peers
            my_file_path  #file path for my relevance judgment file
            .file_list    #contains list of files

        Methods:
            get_file_list() - get_file_list iterates file in the directory.
            get_my_file_dic() - opens file, read line by line, and stores necessary information - query id, document id, relevancy -
            to the given data structure.
            get_kappa_coefficient() - computes k for two files - my file and current relevance judgement file in file list.
            write_file() - Create a file named "kappa_statistics_file.txt" which will contain k values.


    2. RelevancyList:
    RelevancyList class holds data - document id and relevancy.

        Data fields:
            doc_id      #document id
            relevancy   # relevancy which is either 0 or 1

        Methods:
            get_document_id() - returns document_id
            get_relevancy() - returns relevancy

