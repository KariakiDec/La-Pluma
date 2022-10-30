package cn.earthsky.dev.project.lapluma.common.text;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data @AllArgsConstructor
public class AVGCharacter {
    String identity;
    int position;
    @Setter boolean dimmed = false;
}
