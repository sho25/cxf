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
name|jaxrs
operator|.
name|cors
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Inherited
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * Attach<a href="http://www.w3.org/TR/cors/">CORS</a> information  * to a resource. This annotation is read by {@link CrossOriginResourceSharingFilter}.  * If this annotation is present on a method, or   * on the method's class (or its superclasses), then it completely  * overrides any parameters set in {@link CrossOriginResourceSharingFilter}.   * If a particular parameter of this annotation is not specified, then the  * default value is used,<em>not</em> the parameters of the filter.   *   * Note that the CORS specification censors the headers on a   * preflight OPTIONS request. As a result, the filter cannot determine  * exactly which method corresponds to the request, and so uses only   * class-level annotations to set policies.  */
end_comment

begin_annotation_defn
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|,
name|ElementType
operator|.
name|METHOD
block|}
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Inherited
specifier|public
annotation_defn|@interface
name|CrossOriginResourceSharing
block|{
comment|/**      * If true, this resource will return      *<pre>Access-Control-Allow-Origin: *</pre>      * for a valid request.      */
name|boolean
name|allowAllOrigins
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * A list of permitted origins. This is ignored       * if {@link #allowAllOrigins()} is true.      */
name|String
index|[]
name|allowOrigins
argument_list|()
expr|default
block|{ }
expr_stmt|;
comment|/**      * A list of headers that the client may include      * in an actual request.      */
name|String
index|[]
name|allowHeaders
argument_list|()
expr|default
block|{ }
expr_stmt|;
comment|/**      * If true, this resource will return       *<pre>Access-Control-Allow-Credentials: true</pre>      */
name|boolean
name|allowCredentials
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**      * A list of headers to return in<tt>      * Access-Control-Expose-Headers</tt>.       */
name|String
index|[]
name|exposeHeaders
argument_list|()
expr|default
block|{ }
expr_stmt|;
comment|/**      * The value to return in<tt>Access-Control-Max-Age</tt>.      * If this is negative, then no header is returned. The default      * value is -1.      */
name|int
name|maxAge
parameter_list|()
default|default
operator|-
literal|1
function_decl|;
comment|/**      * Controls the implementation of preflight processing       * on an OPTIONS method.      * If the current method is OPTIONS, and this method wants to       * handle the preflight process for itself, set this value to       *<tt>true</tt>. In the default, false, case, the filter      * performs preflight processing.      */
name|boolean
name|localPreflight
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

