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
name|transport
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Message
import|;
end_import

begin_comment
comment|/**  * This abstract class is extended by developers who need HTTP Auth  * functionality on the client side. It supplies Authorization   * information to an HTTPConduit.  *<p>  * The HTTPConduit will make a call to getPreemptiveAuthorization before  * an HTTP request is made. The HTTPConduit will call on   * getAuthorizationForRealm upon getting a 401 HTTP Response with a  * "WWW-Authenticate: Basic realm=????" header.   *<p>  * A HTTPConduit keeps a reference to this HttpAuthSupplier for the life  * of the HTTPConduit, unless changed out by dynamic configuration.  * Therefore, an implementation of this HttpAuthSupplier may maintain  * state for subsequent calls.   *<p>  * For instance, an implementation may not provide a Authorization preemptively for   * a particular URL and decide to get the realm information from   * a 401 response in which the HTTPConduit will call getAuthorizationForReam for  * that URL. Then this implementation may provide the Authorization for this  * particular URL preemptively for subsequent calls to getPreemptiveAuthorization.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|HttpAuthSupplier
block|{
comment|/**      * This field contains the logical name of this HttpBasicAuthSuppler.      * This field is not assigned to be final, since an extension may be      * Spring initialized as a bean, have an appropriate setLogicalName      * method, and set this field.      */
specifier|protected
name|String
name|logicalName
decl_stmt|;
comment|/**      * The default constructor assigns the class name as the LogicalName.      *      */
specifier|protected
name|HttpAuthSupplier
parameter_list|()
block|{
name|logicalName
operator|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
comment|/**      * This constructor assigns the LogicalName of this HttpBasicAuthSupplier.      *       * @param name The Logical Name.      */
specifier|protected
name|HttpAuthSupplier
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|logicalName
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * This method returns the LogicalName of this HttpBasicAuthSupplier.      */
specifier|public
name|String
name|getLogicalName
parameter_list|()
block|{
return|return
name|logicalName
return|;
block|}
comment|/**      * The HTTPConduit makes a call to this method before connecting      * to the server behind a particular URL. If this implementation does not       * have a Authorization for this URL, it should return null.      *       * @param conduit     The HTTPConduit making the call.      * @param currentURL  The URL to which the request is to be made.      * @param message     The CXF Message.      *       * @return This method returns null if no Authorization is available.      */
specifier|public
specifier|abstract
name|String
name|getPreemptiveAuthorization
parameter_list|(
name|HTTPConduit
name|conduit
parameter_list|,
name|URL
name|currentURL
parameter_list|,
name|Message
name|message
parameter_list|)
function_decl|;
comment|/**      * The HTTPConduit makes a call to this method if it      * receives a 401 response to a particular URL for      * a given message. The realm information is taken      * from the "WWW-Authenticate: ???? realm=?????"      * header. The current message may be retransmitted      * if this call returns a Authorization. The current message will      * fail with a 401 if null is returned. If no Authorization is available      * for this particular URL, realm, and message, then null      * should be returned.      *       * @param conduit     The conduit making the call.      * @param currentURL  The current URL from which the reponse came.      * @param message     The CXF Message.      * @param realm       The realm extraced from the basic auth header.      * @param fullHeader  The full WWW-Authenticate header      * @return      */
specifier|public
specifier|abstract
name|String
name|getAuthorizationForRealm
parameter_list|(
name|HTTPConduit
name|conduit
parameter_list|,
name|URL
name|currentURL
parameter_list|,
name|Message
name|message
parameter_list|,
name|String
name|realm
parameter_list|,
name|String
name|fullHeader
parameter_list|)
function_decl|;
block|}
end_class

end_unit

