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
name|cdi
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
name|javax
operator|.
name|enterprise
operator|.
name|util
operator|.
name|AnnotationLiteral
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Qualifier
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
name|jaxrs
operator|.
name|ext
operator|.
name|ContextProvider
import|;
end_import

begin_comment
comment|/**  * ContextResolved is an internal qualifier used by CXF to differentiate the beans it will manage from  * beans a user may have provided.  A user should not use this qualifier, but all beans that CXF provides  * that are from {@link javax.ws.rs.core.Context} objects.  *  * Likewise, for any field level injections, as well as constructor injections, the CDI instance of the  * Context object will be used.  Methods annotated {@link javax.inject.Inject} will also delegate to CDI.  * Any method parameter that takes a Context object will still be resolved from non-CDI semantics.  *  * For all built in context objects (as defined by the JAX-RS specification), the thread local aware instance  * is used.  For any custom context objects (implemented via {@link ContextProvider}) you must ensure that  * they are implemented in a thread safe manner.  All context objects are backed by a  * {@link javax.enterprise.context.RequestScoped} bean.  */
end_comment

begin_annotation_defn
annotation|@
name|Qualifier
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
specifier|public
annotation_defn|@interface
name|ContextResolved
block|{
name|ContextResolved
name|LITERAL
init|=
operator|new
name|ContextResolvedLiteral
argument_list|()
decl_stmt|;
specifier|final
class|class
name|ContextResolvedLiteral
extends|extends
name|AnnotationLiteral
argument_list|<
name|ContextResolved
argument_list|>
implements|implements
name|ContextResolved
block|{      }
block|}
end_annotation_defn

end_unit

