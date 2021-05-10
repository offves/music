package com.offves.music.stream.event;

import com.offves.stream.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlaylistEvent extends Event {

    private Long playlistId;

}
