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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|aegis
operator|.
name|type
operator|.
name|AegisType
import|;
end_import

begin_comment
comment|/**  * Interface for Aegis writers.  * @param<SinkT>  */
end_comment

begin_interface
specifier|public
interface|interface
name|AegisWriter
parameter_list|<
name|SinkT
parameter_list|>
extends|extends
name|AegisIo
block|{
comment|/**      * Write an object to the sink.      * @param obj The object.      * @param elementName The element QName.      * @param optional true to omit for null. (minOccurs=0)      * @param output The output sink.      * @param aegisType The aegis type to use. Null is allowed, but only if      * obj is not null.       * @throws Exception      */
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|QName
name|elementName
parameter_list|,
name|boolean
name|optional
parameter_list|,
name|SinkT
name|output
parameter_list|,
name|AegisType
name|aegisType
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Write an object to the sink, providing a {@link java.lang.reflect.Type} to specify      * its type.      * @param obj the object      * @param elementName XML element name      * @param optional true if null maps to no output at all.      * @param output where to put it.      * @param objectType A description of the type of the object.      * @throws Exception      */
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|QName
name|elementName
parameter_list|,
name|boolean
name|optional
parameter_list|,
name|SinkT
name|output
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
name|objectType
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

