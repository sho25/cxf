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

begin_comment
comment|/**  * Non-parameterized base interface for DataReader&lt;T&gt;  */
end_comment

begin_interface
specifier|public
interface|interface
name|BaseDataReader
block|{
name|String
name|FAULT
init|=
name|DataReader
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"Fault"
decl_stmt|;
name|String
name|ENDPOINT
init|=
name|DataReader
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"Endpoint"
decl_stmt|;
comment|/**      * Supply a schema to validate the input. Bindings silently ignore this parameter if they      * do not support schema validation, or the particular form of validation implied by      * a particular form of Schema.      * @param s      */
name|void
name|setSchema
parameter_list|(
name|Schema
name|s
parameter_list|)
function_decl|;
comment|/**      * Attach a collection of attachments to a binding. This permits a binding to process the contents      * of one or more attachments as part of reading from this reader.      * @param attachments attachments.      */
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
comment|/**      * Set an arbitrary property on the reader.      * {@link #FAULT} and {@link #ENDPOINT} specify two common properties: the Fault object being read      * and the {@link org.apache.cxf.endpoint.Endpoint}.      * @param prop Name of the property.      * @param value Value of the property.      */
name|void
name|setProperty
parameter_list|(
name|String
name|prop
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

