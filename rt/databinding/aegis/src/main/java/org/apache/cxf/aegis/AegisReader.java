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
name|aegis
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|aegis
operator|.
name|type
operator|.
name|AegisType
import|;
end_import

begin_comment
comment|/**  * Interface for Aegis readers.  * @param<ReaderT>  */
end_comment

begin_interface
specifier|public
interface|interface
name|AegisReader
parameter_list|<
name|ReaderT
parameter_list|>
extends|extends
name|AegisIo
block|{
comment|/**      * Read an object.      * @param reader the source.      * @return      * @throws Exception      */
name|Object
name|read
parameter_list|(
name|ReaderT
name|reader
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Read an object expecting a particular input type.      * @param reader the source.      * @param desiredType the expected input type.      * @return      * @throws Exception      */
name|Object
name|read
parameter_list|(
name|ReaderT
name|reader
parameter_list|,
name|AegisType
name|desiredType
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

