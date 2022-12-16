package cn.earthsky.dev.project.lapluma.common;

import cn.earthsky.dev.project.lapluma.common.text.ConversationLoader;
import cn.earthsky.dev.project.lapluma.common.text.ConversationStructure;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class JournalNamespace {
    private final static ConcurrentHashMap<String, ConversationStructure> namespaced = new ConcurrentHashMap<>();

    private final static Function<String,ConversationStructure> loader = journal -> {
        ConversationStructure str = ConversationLoader.loadStructureFromResource(journal);
        if(str != null)
            namespaced.put(journal, str);
        return str;
    };

    public static ConversationStructure get(String namespace){
        if(namespaced.containsKey(namespace)){
            return namespaced.get(namespace);
        }else{
            return loader.apply(namespace);
        }
    }


    public static void put(String namespace, ConversationStructure str){
        namespaced.put(namespace, str);
    }
}
