package x.none.config;

import lombok.Getter;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@RequestScope
public class ItemCountListener implements ChunkListener {

    private int count;

    @Override
    public void beforeChunk(ChunkContext chunkContext) {

    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
        count = chunkContext.getStepContext().getStepExecution().getReadCount();
    }

    @Override
    public void afterChunkError(ChunkContext chunkContext) {
        count = chunkContext.getStepContext().getStepExecution().getReadCount();
    }
}
