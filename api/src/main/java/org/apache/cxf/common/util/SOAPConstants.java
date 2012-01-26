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
name|common
operator|.
name|util
package|;
end_package

begin_comment
comment|/**  * SOAP constants from the specifications.  *   * @author<a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>  * @since Feb 18, 2004  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SOAPConstants
block|{
comment|/** Document styles. */
specifier|public
specifier|static
specifier|final
name|String
name|WSDL11_NS
init|=
literal|"http://schemas.xmlsoap.org/wsdl/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSDL11_SOAP_NS
init|=
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
decl_stmt|;
comment|/**      * Constant used to specify a rpc binding style.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_RPC
init|=
literal|"rpc"
decl_stmt|;
comment|/**      * Constant used to specify a document binding style.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_DOCUMENT
init|=
literal|"document"
decl_stmt|;
comment|/**      * Constant used to specify a wrapped binding style.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_WRAPPED
init|=
literal|"wrapped"
decl_stmt|;
comment|/**      * Constant used to specify a message binding style.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_MESSAGE
init|=
literal|"message"
decl_stmt|;
comment|/**      * Constant used to specify a literal binding use.      */
specifier|public
specifier|static
specifier|final
name|String
name|USE_LITERAL
init|=
literal|"literal"
decl_stmt|;
comment|/**      * Constant used to specify a encoded binding use.      */
specifier|public
specifier|static
specifier|final
name|String
name|USE_ENCODED
init|=
literal|"encoded"
decl_stmt|;
comment|/**      * XML Schema Namespace.      */
specifier|public
specifier|static
specifier|final
name|String
name|XSD
init|=
literal|"http://www.w3.org/2001/XMLSchema"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XSD_PREFIX
init|=
literal|"xsd"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XSI_NS
init|=
literal|"http://www.w3.org/2001/XMLSchema-instance"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XSI_PREFIX
init|=
literal|"xsi"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MEP_ROBUST_IN_OUT
init|=
literal|"urn:xfire:mep:in-out"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MEP_IN
init|=
literal|"urn:xfire:mep:in"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_ACTION
init|=
literal|"SOAPAction"
decl_stmt|;
comment|/**      * Whether or not MTOM should be enabled for each service.      */
specifier|public
specifier|static
specifier|final
name|String
name|MTOM_ENABLED
init|=
literal|"mtom-enabled"
decl_stmt|;
specifier|private
name|SOAPConstants
parameter_list|()
block|{
comment|//utility class
block|}
block|}
end_class

end_unit

