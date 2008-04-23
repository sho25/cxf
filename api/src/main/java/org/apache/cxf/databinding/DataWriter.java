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
name|databinding
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
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
name|message
operator|.
name|Attachment
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
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
import|;
end_import

begin_comment
comment|/**  * The 'write' side of the data binding abstraction of CXF. A DataWriter&lt;T&gt; serializes  * objects to a 'sink' of type T.  * @param<T> The type of sink. Each data binding defines the set of sink types that it supports.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataWriter
parameter_list|<
name|T
parameter_list|>
block|{
name|String
name|ENDPOINT
init|=
name|DataWriter
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"Endpoint"
decl_stmt|;
comment|/**      * Write an object to an output sink.      * @param obj the object to write.      * @param output the output sink.      */
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|T
name|output
parameter_list|)
function_decl|;
comment|/**      * Write an object to an output sink, including extra processing based on the WSDL       * service model for a particular message part.      * @param obj The object to write.      * @param part the message part.       * @param output the output sink.      */
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|MessagePartInfo
name|part
parameter_list|,
name|T
name|output
parameter_list|)
function_decl|;
comment|/**      * Attach a schema to the writer. If the binding supports validation, it will      * validate the XML that it produces (assuming that it produces XML).       * @param s the schema.      */
name|void
name|setSchema
parameter_list|(
name|Schema
name|s
parameter_list|)
function_decl|;
comment|/**      * Attach a collection of attachments to this writer.      * @param attachments      */
name|void
name|setAttachments
parameter_list|(
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
function_decl|;
comment|/**      * Set a property for the writer.      * @param key property key       * @param value property value.      */
name|void
name|setProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

