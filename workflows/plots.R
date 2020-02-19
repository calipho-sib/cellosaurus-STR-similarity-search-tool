library(ggplot2)
library(dplyr)
library(reshape2)
library(grid)
library(gridExtra)
library(forcats)

#==================================================================================================
# Functions 
#==================================================================================================   

outliers.lower.counts <- function(r,s) {
  b <- boxplot.stats(s)
  length(b$out[b$out <= b$stats[1]])
}
  
outliers.higher.counts <- function(r,s) {
  b <- boxplot.stats(s)
  length(b$out[b$out >= b$stats[5]])
}

outliers.lower.limits <- function(r,s) {
  b <- boxplot.stats(s)
  o <-b$out[b$out <=b$stats[1]]
  if (any(r=="Related")) {
    length(o[o <=80])
  } else {
    length(o[o >=80])
  }
}

outliers.higher.limits <- function(r,s) {
  b <- boxplot.stats(s)
  o <-b$out[b$out >=b$stats[5]]
  if (any(r=="Related")) {
    length(o[o <=80])
  } else {
    length(o[o >=80])
  }
}

stats.summary <- function(df) {
  group_by(df, relationship, markers, parameter) %>% summarize(
    mean=round(mean(score), digits=2),
    median=median(score), 
    outliers.lower=outliers.lower.counts(relationship, score),
    outliers.lower.80=outliers.lower.limits(relationship, score),
    outliers.higher=outliers.higher.counts(relationship, score),
    outliers.higher.80=outliers.higher.limits(relationship, score)
  )
}

filter.cell <- function(df, c) {
  df[(df$sample1==c | df$sample2==c),]
}

#==================================================================================================
# Run Algorithms
#==================================================================================================                            
  
df1 <- read.table(file='results/data/Run_Algorithms-8_STR_Markers-Tanabe.tsv',sep='\t',header=T)
df2 <- read.table(file='results/data/Run_Algorithms-8_STR_Markers-Masters.tsv',sep='\t',header=T)
df3 <- read.table(file='results/data/Run_Algorithms-8_STR_Markers-Reverse.tsv',sep='\t',header=T)
df4 <- read.table(file='results/data/Run_Algorithms-13_STR_Markers-Tanabe.tsv',sep='\t',header=T)
df5 <- read.table(file='results/data/Run_Algorithms-13_STR_Markers-Masters.tsv',sep='\t',header=T)
df6 <- read.table(file='results/data/Run_Algorithms-13_STR_Markers-Reverse.tsv',sep='\t',header=T)

df3$parameter <- "Masters\n(Reverse)"
df6$parameter <- "Masters\n(Reverse)"

df <- rbind(df1, df2, df3, df4, df5, df6)

p <- ggplot(data=df, aes(x=parameter, x2=relationship, y=score, fill=relationship)) +
  geom_hline(yintercept=80, color="#ebebeb") +
  geom_hline(yintercept=80, linetype="dashed", color="#999999") +
  geom_violin(scale="width", adjust=2, aes(color=relationship), size=0.2) +
  stat_boxplot(position=position_dodge(width=0.9), geom='errorbar', color="#333333", width=0.4) +
  geom_boxplot(position=position_dodge(width=0.9), width=0.4, fill="white", outlier.size=0.75, outlier.alpha=0.1) +
  stat_summary(position=position_dodge(width=0.9), fun.y=mean, geom="point", color="#333333", shape=22, size=2, show.legend=F) +
  facet_grid(~markers) +
  scale_x_discrete(name="Scoring Algorithm") +
  scale_y_continuous(name="STR Similarity [%]", breaks=seq(from=0,to=100,by=10), limits=c(0,100), expand=c(.025,.025)) +
  scale_fill_manual(name="Pair Relationship", values=c("#83b4e4","#ff988d"), aesthetics=c("color", "fill")) +
  labs(title = "STR Similarity Score Distributions",
       subtitle = "Influence of the Scoring Algorithm") +
  theme(plot.title=element_text(size=12,face="bold",hjust=.5),
        plot.subtitle=element_text(size=11,face="bold",hjust=.5, color="#444444"),
        strip.text.x=element_text(size=10),
        axis.text.x=element_text(size=10),
        axis.ticks.x=element_blank(),
        legend.position="bottom")

ggsave(path="results/plots",filename="Run_Algorithms.png", device="png", plot=p, width=6.5, height=4.5)

s <- stats.summary(df)
write.table(s, file="results/tables/Run_Algorithms.tsv", row.names=F)

df.m14 <- filter.cell(df, "CVCL_1395")
df.jurkat <- filter.cell(df, "CVCL_0065")

df.m14$dataset <- "M14 (MSS)"
df.jurkat$dataset <- "Jurkat (MSI-High)"
df <- rbind(df.m14, df.jurkat)

p <- ggplot(data=df, aes(x=parameter, x2=relationship, y=score, fill=relationship)) +
  geom_hline(yintercept=80, color="#ebebeb") +
  geom_hline(yintercept=80, linetype="dashed", color="#999999") +
  geom_violin(scale="width", adjust=1, aes(color=relationship), size=0.2) +
  stat_boxplot(position=position_dodge(width=0.9), geom='errorbar', color="#333333", width=0.4) +
  geom_boxplot(position=position_dodge(width=0.9), width=0.4, fill="white", outlier.size=1, outlier.alpha=1) +
  stat_summary(position=position_dodge(width=0.9), fun.y=mean, geom="point", color="#333333", shape=22, size=2, show.legend=F) +
  facet_grid(vars(dataset), vars(markers)) +
  scale_x_discrete(name="Scoring Algorithm") +
  scale_y_continuous(name="STR Similarity [%]", breaks=seq(from=0,to=100,by=10), limits=c(0,100), expand=c(.025,.025)) +
  scale_fill_manual(name="Pair Relationship", values=c("#83b4e4","#ff988d"), aesthetics=c("color", "fill")) +
  labs(title = "STR Similarity Score Distributions",
       subtitle = "Influence of the Scoring Algorithm - Jurkat/M14 Cell Lines") +
  theme(plot.title=element_text(size=12,face="bold",hjust=.5),
        plot.subtitle=element_text(size=11,face="bold",hjust=.5, color="#444444"),
        strip.text.x=element_text(size=10),
        axis.text.x=element_text(size=10),
        axis.ticks.x=element_blank(),
        legend.position="bottom")

ggsave(path="results/plots",filename="Run_Algorithms_Cells.png", device="png", plot=p, width=6.5, height=6)

#==================================================================================================
# Run Homozygoztes
#================================================================================================== 

df1 <- read.table(file='results/data/Run_Homozygotes-8_STR_Markers-As_One.tsv',sep='\t',header=T)
df2 <- read.table(file='results/data/Run_Homozygotes-8_STR_Markers-As_Two.tsv',sep='\t',header=T)
df3 <- read.table(file='results/data/Run_Homozygotes-13_STR_Markers-As_One.tsv',sep='\t',header=T)
df4 <- read.table(file='results/data/Run_Homozygotes-13_STR_Markers-As_Two.tsv',sep='\t',header=T)

df <- rbind(df1, df2, df3, df4)

p <- ggplot(data=df, aes(x=parameter, x2=relationship, y=score, fill=relationship)) +
  geom_hline(yintercept=80, color="#ebebeb") +
  geom_hline(yintercept=80, linetype="dashed", color="#999999") +
  geom_violin(scale="width", adjust=2, aes(color=relationship)) +
  stat_boxplot(position=position_dodge(width=0.9), geom='errorbar', color="#333333", width=0.4) +
  geom_boxplot(position=position_dodge(width=0.9), width=0.4, fill="white", outlier.size=0.75, outlier.alpha=0.1) +
  stat_summary(position=position_dodge(width=0.9), fun.y=mean, geom="point", color="#333333", shape=22, size=2, show.legend=F) +
  facet_grid(~markers) +
  scale_x_discrete(name="Homozygous Locus Count") +
  scale_y_continuous(name="STR Similarity [%]", breaks=seq(from=0,to=100,by=10), limits=c(0,100), expand=c(.025,.025)) +
  scale_fill_manual(name="Pair Relationship", values=c("#83b4e4","#ff988d"), aesthetics=c("color", "fill")) +
  labs(title = "STR Similarity Score Distributions",
       subtitle = "Influence of the Homozygous Locus Count") +
  theme(plot.title=element_text(size=12,face="bold",hjust=.5),
        plot.subtitle=element_text(size=11,face="bold",hjust=.5, color="#444444"),
        strip.text.x=element_text(size=10),
        axis.text.x=element_text(size=10),
        axis.ticks.x=element_blank(),
        legend.position="bottom")

ggsave(path="results/plots",filename="Run_Homozygotes.png", device="png", plot=p, width=6.5, height=4.5)

s <- stats.summary(df)
write.table(s, file="results/tables/Run_Homozygotes.tsv", row.names=F)

df.m14 <- filter.cell(df, "CVCL_1395")
df.jurkat <- filter.cell(df, "CVCL_0065")

df.m14$dataset <- "M14 (MSS)"
df.jurkat$dataset <- "Jurkat (MSI-High)"
df <- rbind(df.m14, df.jurkat)

p <- ggplot(data=df, aes(x=parameter, x2=relationship, y=score, fill=relationship)) +
  geom_hline(yintercept=80, color="#ebebeb") +
  geom_hline(yintercept=80, linetype="dashed", color="#999999") +
  geom_violin(scale="width", adjust=1, aes(color=relationship)) +
  stat_boxplot(position=position_dodge(width=0.9), geom='errorbar', color="#333333", width=0.4) +
  geom_boxplot(position=position_dodge(width=0.9), width=0.4, fill="white", outlier.size=1, outlier.alpha=1) +
  stat_summary(position=position_dodge(width=0.9), fun.y=mean, geom="point", color="#333333", shape=22, size=2, show.legend=F) +
  facet_grid(vars(dataset), vars(markers)) +
  scale_x_discrete(name="Homozygous Locus Count") +
  scale_y_continuous(name="STR Similarity [%]", breaks=seq(from=0,to=100,by=10), limits=c(0,100), expand=c(.025,.025)) +
  scale_fill_manual(name="Pair Relationship", values=c("#83b4e4","#ff988d"), aesthetics=c("color", "fill")) +
  labs(title = "STR Similarity Score Distributions",
       subtitle = "Influence of the Homozygous Locus Count - Jurkat/M14 Cell Lines") +
  theme(plot.title=element_text(size=12,face="bold",hjust=.5),
        plot.subtitle=element_text(size=11,face="bold",hjust=.5, color="#444444"),
        strip.text.x=element_text(size=10),
        axis.text.x=element_text(size=10),
        axis.ticks.x=element_blank(),
        legend.position="bottom")

ggsave(path="results/plots",filename="Run_Homozygotes_Cells.png", device="png", plot=p, width=6.5, height=6)

#==================================================================================================
# Run Homozygoztes B
#================================================================================================== 

df1 <- read.table(file='results/data/Run_Homozygotes-8_STR_Markers-As_One.tsv',sep='\t',header=T)
df2 <- read.table(file='results/data/Run_Homozygotes-8_STR_Markers-As_Two.tsv',sep='\t',header=T)

p1 <- ggplot(data=subset(df1,relationship=="Unrelated"),aes(x=score)) + 
  stat_bin(binwidth=.5, fill="#ff988d",show.legend=F) +  
  facet_grid(~parameter) +
  scale_y_continuous(name="Count",limits=c(0,125000),breaks=seq(from=0,to=125000,by=25000)) +
  scale_x_continuous(name="STR Similarity [%]",limits=c(-1,75),breaks=seq(from=0,to=75,by=10)) +
  theme(strip.text.x=element_text(size=10),
        axis.text.x=element_text(size=10),
        axis.ticks.x=element_blank())

p2 <- ggplot(data=subset(df2,relationship=="Unrelated"),aes(x=score)) + 
  stat_bin(binwidth=.5, fill="#ff988d",show.legend=F) +  
  stat_bin(binwidth=.5, geom="text", aes(label=ifelse(..count..>=4e+04,paste(round(x/100*16,1), "/16", sep=""),"")), fontface="bold", vjust=-.1) +
  facet_grid(~parameter) +
  annotate("text",x=0,y=9000, label="0/16",fontface="bold", vjust=-.1) +
  annotate("text",x=62.5,y=20000, label="10/16",fontface="bold", vjust=-.1) +
  annotate("text",x=68.75,y=5000, label="11/16",fontface="bold", vjust=-.1) +
  scale_y_continuous(name="Count",limits=c(0,5.1e5),breaks=seq(from=0,to=5.1e5,by=1e5)) +
  scale_x_continuous(name="STR Similarity [%]",limits=c(-1,75),breaks=seq(from=0,to=75,by=10)) +
  theme(strip.text.x=element_text(size=10),
        axis.text.x=element_text(size=10),
        axis.ticks.x=element_blank())

p <- grid.arrange(p1,p2,ncol=2, top=textGrob("Unrelated Homozygous Locus Count",gp=gpar(fontsize=12,fontface="bold")))
ggsave(path="results/plots",filename="Run_Homozygotes_B.png", device="png", plot=p,width=7,height=4)

#==================================================================================================
# Run Amelogenin
#================================================================================================== 

df1 <- read.table(file='results/data/Run_Amelogenin-8_STR_Markers-Not_Included.tsv',sep='\t',header=T)
df2 <- read.table(file='results/data/Run_Amelogenin-8_STR_Markers-Included.tsv',sep='\t',header=T)
df3 <- read.table(file='results/data/Run_Amelogenin-13_STR_Markers-Not_Included.tsv',sep='\t',header=T)
df4 <- read.table(file='results/data/Run_Amelogenin-13_STR_Markers-Included.tsv',sep='\t',header=T)

df <- rbind(df1, df2, df3, df4)

p <- ggplot(data=df, aes(x=parameter, x2=relationship, y=score, fill=relationship)) +
  geom_hline(yintercept=80, color="#ebebeb") +
  geom_hline(yintercept=80, linetype="dashed", color="#999999") +
  geom_violin(scale="width", adjust=2, aes(color=relationship)) +
  stat_boxplot(position=position_dodge(width=0.9), geom='errorbar', color="#333333", width=0.4) +
  geom_boxplot(position=position_dodge(width=0.9), width=0.4, fill="white", outlier.size=0.75, outlier.alpha=0.1) +
  stat_summary(position=position_dodge(width=0.9), fun.y=mean, geom="point", color="#333333", shape=22, size=2, show.legend=F) +
  facet_grid(~markers) +
  scale_x_discrete(name="Amelogenin") +
  scale_y_continuous(name="STR Similarity [%]", breaks=seq(from=0,to=100,by=10), limits=c(0,100), expand=c(.025,.025)) +
  scale_fill_manual(name="Pair Relationship", values=c("#83b4e4","#ff988d"), aesthetics=c("color", "fill")) +
  labs(title = "STR Similarity Score Distributions",
       subtitle = "Influence of the Inclusion of Amelogenin") +
  theme(plot.title=element_text(size=12,face="bold",hjust=.5),
        plot.subtitle=element_text(size=11,face="bold",hjust=.5, color="#444444"),
        strip.text.x=element_text(size=10),
        axis.text.x=element_text(size=10),
        axis.ticks.x=element_blank(),
        legend.position="bottom")

ggsave(path="results/plots",filename="Run_Amelogenin.png", device="png", plot=p, width=6.5, height=4.5)

s <- stats.summary(df)
write.table(s, file="results/tables/Run_AmelogeninY.tsv", row.names=F)

df.m14 <- filter.cell(df, "CVCL_1395")
df.jurkat <- filter.cell(df, "CVCL_0065")

df.m14$dataset <- "M14 (MSS)"
df.jurkat$dataset <- "Jurkat (MSI-High)"
df <- rbind(df.m14, df.jurkat)

p <- ggplot(data=df, aes(x=parameter, x2=relationship, y=score, fill=relationship)) +
  geom_hline(yintercept=80, color="#ebebeb") +
  geom_hline(yintercept=80, linetype="dashed", color="#999999") +
  geom_violin(scale="width", adjust=1, aes(color=relationship)) +
  stat_boxplot(position=position_dodge(width=0.9), geom='errorbar', color="#333333", width=0.4) +
  geom_boxplot(position=position_dodge(width=0.9), width=0.4, fill="white", outlier.size=1, outlier.alpha=1) +
  stat_summary(position=position_dodge(width=0.9), fun.y=mean, geom="point", color="#333333", shape=22, size=2, show.legend=F) +
  facet_grid(vars(dataset), vars(markers)) +
  scale_x_discrete(name="Amelogenin") +
  scale_y_continuous(name="STR Similarity [%]", breaks=seq(from=0,to=100,by=10), limits=c(0,100), expand=c(.025,.025)) +
  scale_fill_manual(name="Pair Relationship", values=c("#83b4e4","#ff988d"), aesthetics=c("color", "fill")) +
  labs(title = "STR Similarity Score Distributions",
       subtitle = "Influence of the Inclusion of Amelogenin - Jurkat/M14 Cell Lines") +
  theme(plot.title=element_text(size=12,face="bold",hjust=.5),
        plot.subtitle=element_text(size=11,face="bold",hjust=.5, color="#444444"),
        strip.text.x=element_text(size=10),
        axis.text.x=element_text(size=10),
        axis.ticks.x=element_blank(),
        legend.position="bottom")

ggsave(path="results/plots",filename="Run_Amelogenin_Cells.png", device="png", plot=p, width=6.5, height=6)

#==================================================================================================
# Run Markers
#==================================================================================================

df1 <- read.table(file='results/data/Run_Markers-1_STR_Markers-1.tsv',sep='\t',header=T)
df2 <- read.table(file='results/data/Run_Markers-2_STR_Markers-2.tsv',sep='\t',header=T)
df3 <- read.table(file='results/data/Run_Markers-3_STR_Markers-3.tsv',sep='\t',header=T)
df4 <- read.table(file='results/data/Run_Markers-4_STR_Markers-4.tsv',sep='\t',header=T)
df5 <- read.table(file='results/data/Run_Markers-5_STR_Markers-5.tsv',sep='\t',header=T)
df6 <- read.table(file='results/data/Run_Markers-6_STR_Markers-6.tsv',sep='\t',header=T)
df7 <- read.table(file='results/data/Run_Markers-7_STR_Markers-7.tsv',sep='\t',header=T)
df8 <- read.table(file='results/data/Run_Markers-8_STR_Markers-8.tsv',sep='\t',header=T)
df9 <- read.table(file='results/data/Run_Markers-9_STR_Markers-9.tsv',sep='\t',header=T)
df10 <- read.table(file='results/data/Run_Markers-10_STR_Markers-10.tsv',sep='\t',header=T)
df11 <- read.table(file='results/data/Run_Markers-11_STR_Markers-11.tsv',sep='\t',header=T)
df12 <- read.table(file='results/data/Run_Markers-12_STR_Markers-12.tsv',sep='\t',header=T)
df13 <- read.table(file='results/data/Run_Markers-13_STR_Markers-13.tsv',sep='\t',header=T)
df14 <- read.table(file='results/data/Run_Markers-14_STR_Markers-14.tsv',sep='\t',header=T)
df15 <- read.table(file='results/data/Run_Markers-15_STR_Markers-15.tsv',sep='\t',header=T)
df16 <- read.table(file='results/data/Run_Markers-16_STR_Markers-16.tsv',sep='\t',header=T)
df17 <- read.table(file='results/data/Run_Markers-17_STR_Markers-17.tsv',sep='\t',header=T)

df <- rbind(df1, df2, df3, df4, df5, df6, df7, df8, df9, df10, df11, df12, df13, df14, df15, df16, df17)

p <- ggplot(data=df, aes(x=factor(parameter), y=score, fill=relationship)) +
  geom_hline(yintercept=80, color="#ebebeb") +
  geom_hline(yintercept=80, linetype="dashed", color="#999999") +
  stat_boxplot(geom='errorbar', color="#333333") +
  geom_boxplot(outlier.size=0.75, outlier.alpha=0.1) +
  scale_x_discrete(name="Number of STR Markers") +
  scale_y_continuous(name="STR Similarity [%]", breaks=seq(from=0,to=100,by=10), limits=c(0,100), expand=c(.025,.025)) +
  scale_fill_manual(name="Pair Relationship", values=c("#83b4e4","#ff988d")) +
  labs(title = "STR Similarity Score Distributions",
       subtitle = "Influence of the Number of STR Markers") +
  theme(plot.title=element_text(size=12,face="bold",hjust=.5),
        plot.subtitle=element_text(size=11,face="bold",hjust=.5, color="#444444"),
        strip.text.x=element_text(size=10),
        axis.text.x=element_text(size=10),
        axis.ticks.x=element_blank(),
        legend.position="bottom")

ggsave(path="results/plots",filename="Run_Markers.png", device="png", plot=p, width=6.5,height=4.5)

s <- stats.summary(df)
write.table(s, file="results/tables/Run_Markers.tsv", row.names=F)
