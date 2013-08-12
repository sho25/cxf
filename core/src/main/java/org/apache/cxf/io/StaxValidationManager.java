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
name|io
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|ServiceInfo
import|;
end_import

begin_comment
comment|/**  * This interface defines a bus service for Stax validation.  * Initially, this is purely with Woodstox4 and msv.  */
end_comment

begin_interface
specifier|public
interface|interface
name|StaxValidationManager
block|{
comment|/**      * Install the schemas onto the reader for validation.      * @param reader - the reader to enable validation      * @param serviceInfo - the Service to pull the schema from      * @throws XMLStreamException       */
name|void
name|setupValidation
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|ServiceInfo
name|serviceInfo
parameter_list|)
throws|throws
name|XMLStreamException
function_decl|;
comment|/**      * Install the schemas onto the writer for validation.      * @param writer - the writer to enable validation      * @param serviceInfo - the Service to pull the schema from      * @throws XMLStreamException       */
name|void
name|setupValidation
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|ServiceInfo
name|serviceInfo
parameter_list|)
throws|throws
name|XMLStreamException
function_decl|;
block|}
end_interface

end_unit

