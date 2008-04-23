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
name|tools
operator|.
name|common
operator|.
name|toolspec
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|tools
operator|.
name|common
operator|.
name|ToolException
import|;
end_import

begin_interface
specifier|public
interface|interface
name|ToolContext
block|{
comment|/**      * Request an input stream.      * @param id the id of the stream in the streams sections of the tool's definition document.      */
name|InputStream
name|getInputStream
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|ToolException
function_decl|;
comment|/**      * Request the standard input stream.      */
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|ToolException
function_decl|;
comment|/**      * Request a document based on the input stream.      * This is only returned if the mime type of incoming stream is xml.      */
name|Document
name|getInputDocument
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|ToolException
function_decl|;
comment|/**      * Request a document based on the standard input stream.      * This is only returned if the mime type of incoming stream is xml.      */
name|Document
name|getInputDocument
parameter_list|()
throws|throws
name|ToolException
function_decl|;
name|OutputStream
name|getOutputStream
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|ToolException
function_decl|;
name|OutputStream
name|getOutputStream
parameter_list|()
throws|throws
name|ToolException
function_decl|;
name|String
name|getParameter
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|ToolException
function_decl|;
name|String
index|[]
name|getParameters
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|ToolException
function_decl|;
name|boolean
name|hasParameter
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|ToolException
function_decl|;
name|void
name|sendDocument
parameter_list|(
name|String
name|id
parameter_list|,
name|Document
name|doc
parameter_list|)
function_decl|;
name|void
name|sendDocument
parameter_list|(
name|Document
name|doc
parameter_list|)
function_decl|;
name|void
name|executePipeline
parameter_list|()
function_decl|;
name|void
name|setUserObject
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|o
parameter_list|)
function_decl|;
name|Object
name|getUserObject
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

