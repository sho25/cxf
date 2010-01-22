begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|logging
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|SearchCondition
import|;
end_import

begin_comment
comment|/**  * Can be used by pull-style loggers to read the records from some external storage   *  */
end_comment

begin_interface
specifier|public
interface|interface
name|ReadableLogStorage
block|{
comment|/**      * Read the records and load them into a provided list      * @param list the list saved records should be added to      * @param condition the condition loaded records must meet, can be null       * @param loadFrom the initial index of the storage to have records loaded from      * @param int maxNumberOfRecords the max number of records to load from the storage      */
name|void
name|load
parameter_list|(
name|List
argument_list|<
name|LogRecord
argument_list|>
name|list
parameter_list|,
name|SearchCondition
argument_list|<
name|LogRecord
argument_list|>
name|condition
parameter_list|,
name|int
name|loadFrom
parameter_list|,
name|int
name|maxNumberOfRecords
parameter_list|)
function_decl|;
comment|/**      * Get the size of storage (in records)      * @param the size, -1 if not known, for ex, when reading from an open file containing log entries      */
name|int
name|getSize
parameter_list|()
function_decl|;
comment|/**      * Close the storage      */
name|void
name|close
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

