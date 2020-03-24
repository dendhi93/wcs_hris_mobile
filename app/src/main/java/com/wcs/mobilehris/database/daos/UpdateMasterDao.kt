package com.wcs.mobilehris.database.daos

import androidx.room.*
import com.wcs.mobilehris.database.entity.UpdateMasterEntity

@Dao
interface UpdateMasterDao{

    @Query("SELECT * FROM mUpdate_master_data")
    fun getDataUpdate() : List<UpdateMasterEntity>

    @Query("SELECT max(id) FROM mUpdate_master_data")
    fun getDataUpdateMaxId() : Int

    @Query("SELECT count(id) FROM mUpdate_master_data")
    fun getCountDataUpdate() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUpdateMaster(updateMasterEntity: UpdateMasterEntity)

    @Delete
    fun deleteUpdateMaster(updateMasterEntity: UpdateMasterEntity)

    @Query("SELECT * FROM mUpdate_master_data WHERE mTableDescription LIKE :tableDesc")
    fun getDataByDesc(tableDesc : String) : List<UpdateMasterEntity>

}