package com.zrzhen.idmaker.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public abstract class AbstractIdServiceImpl implements IdService {
    public static final long EPOCH = 1420041600000L;

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected long machineId = -1;
    protected long genMethod = 0;
    protected long type = 01;
    protected long version = 0;

    protected IdMeta idMeta;

    protected IdConverter idConverter;


    public AbstractIdServiceImpl() {
        init();
    }


    public void init() {
        this.machineId = 100L;//机器id

        if (machineId < 0) {
            log.error("The machine ID is not configured properly so that Vesta Service refuses to start.");

            throw new IllegalStateException(
                    "The machine ID is not configured properly so that Vesta Service refuses to start.");

        }

        setIdMeta(IdConverterImpl.maxPeak);
        setIdConverter(new IdConverterImpl());
    }

    public long genId() {
        Id id = new Id();

        populateId(id);

        id.setMachine(machineId);
        id.setGenMethod(genMethod);
        id.setType(type);
        id.setVersion(version);


        long ret = idConverter.convert(id);

        // Use trace because it cause low performance
        if (log.isTraceEnabled())
            log.trace(String.format("Id: %s => %d", id, ret));

        return ret;
    }

    protected abstract void populateId(Id id);

    protected long genTime() {

        return (System.currentTimeMillis() - EPOCH);

    }

    public Id expId(long id) {
        return idConverter.convert(id);
    }

    public long makeId(long time, long seq) {
        return makeId(time, seq, machineId);
    }

    public long makeId(long time, long seq, long machine) {
        return makeId(genMethod, time, seq, machine);
    }

    public long makeId(long genMethod, long time, long seq, long machine) {
        return makeId(type, genMethod, time, seq, machine);
    }

    public long makeId(long type, long genMethod, long time,
                       long seq, long machine) {
        return makeId(version, type, genMethod, time, seq, machine);
    }

    public long makeId(long version, long type, long genMethod,
                       long time, long seq, long machine) {

        Id id = new Id(machine, seq, time, genMethod, type, version);
        IdConverter idConverter = new IdConverterImpl();

        return idConverter.convert(id);
    }

    public Date transTime(long time) {
        return new Date(time * 1000 + EPOCH);
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public void setGenMethod(long genMethod) {
        this.genMethod = genMethod;
    }

    public void setType(long type) {
        this.type = type;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void setIdConverter(IdConverter idConverter) {
        this.idConverter = idConverter;
    }

    public void setIdMeta(IdMeta idMeta) {
        this.idMeta = idMeta;
    }

}