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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_comment
comment|/**  * Provides the complete information about a given opaque permission.  * For example, a scope parameter such as "read_calendar" will be  * translated into the instance of this class in order to provide  * the human readable description and optionally restrict it to  * a limited set of HTTP verbs and request URIs  */
end_comment

begin_class
annotation|@
name|XmlRootElement
specifier|public
class|class
name|OAuthPermission
extends|extends
name|Permission
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|httpVerbs
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|uris
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
specifier|public
name|OAuthPermission
parameter_list|()
block|{              }
specifier|public
name|OAuthPermission
parameter_list|(
name|String
name|permission
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|super
argument_list|(
name|permission
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the optional list of HTTP verbs, example,      * "GET" and "POST", etc      * @param httpVerbs the list of HTTP verbs      */
specifier|public
name|void
name|setHttpVerbs
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|httpVerbs
parameter_list|)
block|{
name|this
operator|.
name|httpVerbs
operator|=
name|httpVerbs
expr_stmt|;
block|}
comment|/**      * Gets the optional list of HTTP verbs      * @return the list of HTTP verbs      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getHttpVerbs
parameter_list|()
block|{
return|return
name|httpVerbs
return|;
block|}
comment|/**      * Sets the optional list of relative request URIs      * @param uri the list of URIs      */
specifier|public
name|void
name|setUris
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uris
operator|=
name|uri
expr_stmt|;
block|}
comment|/**      * Gets the optional list of relative request URIs      * @return the list of URIs      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getUris
parameter_list|()
block|{
return|return
name|uris
return|;
block|}
block|}
end_class

end_unit

