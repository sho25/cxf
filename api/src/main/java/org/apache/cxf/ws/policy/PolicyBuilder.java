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
name|ws
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|PolicyReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|PolicyRegistry
import|;
end_import

begin_comment
comment|/**  * PolicyBuilder provides methods to create Policy and PolicyReferenceObjects  * from DOM elements.  */
end_comment

begin_interface
specifier|public
interface|interface
name|PolicyBuilder
block|{
comment|/**      * Creates a PolicyReference object from a DOM element.      *       * @param element the element      * @return the PolicyReference object constructed from the element      */
name|PolicyReference
name|getPolicyReference
parameter_list|(
name|Element
name|element
parameter_list|)
function_decl|;
comment|/**      * Creates a Policy object from an DOM element.      *       * @param element the element      * @return the Policy object constructed from the element      */
name|Policy
name|getPolicy
parameter_list|(
name|Element
name|element
parameter_list|)
function_decl|;
comment|/**      * Creates a Policy object from an InputStream.      *       * @param stream the inputstream      * @return the Policy object constructed from the element      */
name|Policy
name|getPolicy
parameter_list|(
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParserConfigurationException
throws|,
name|SAXException
function_decl|;
comment|/**      * Return the PolicyRegistry associated with the builder      * @return      */
name|PolicyRegistry
name|getPolicyRegistry
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

