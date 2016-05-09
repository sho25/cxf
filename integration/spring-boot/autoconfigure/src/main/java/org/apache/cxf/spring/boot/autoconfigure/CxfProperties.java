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
name|spring
operator|.
name|boot
operator|.
name|autoconfigure
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|constraints
operator|.
name|NotNull
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|constraints
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * {@link ConfigurationProperties} for Apache CXF.  *  * @author Vedran Pavic  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
literal|"cxf"
argument_list|)
specifier|public
class|class
name|CxfProperties
block|{
comment|/**      * Path that serves as the base URI for the services.      */
annotation|@
name|NotNull
annotation|@
name|Pattern
argument_list|(
name|regexp
operator|=
literal|"/[^?#]*"
argument_list|,
name|message
operator|=
literal|"Path must start with /"
argument_list|)
specifier|private
name|String
name|path
init|=
literal|"/services"
decl_stmt|;
specifier|private
specifier|final
name|Servlet
name|servlet
init|=
operator|new
name|Servlet
argument_list|()
decl_stmt|;
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|this
operator|.
name|path
return|;
block|}
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
specifier|public
name|Servlet
name|getServlet
parameter_list|()
block|{
return|return
name|this
operator|.
name|servlet
return|;
block|}
specifier|public
specifier|static
class|class
name|Servlet
block|{
comment|/**          * Servlet init parameters to pass to Apache CXF.          */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|init
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**          * Load on startup priority of the Apache CXF servlet.          */
specifier|private
name|int
name|loadOnStartup
init|=
operator|-
literal|1
decl_stmt|;
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getInit
parameter_list|()
block|{
return|return
name|this
operator|.
name|init
return|;
block|}
specifier|public
name|void
name|setInit
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|init
parameter_list|)
block|{
name|this
operator|.
name|init
operator|=
name|init
expr_stmt|;
block|}
specifier|public
name|int
name|getLoadOnStartup
parameter_list|()
block|{
return|return
name|this
operator|.
name|loadOnStartup
return|;
block|}
specifier|public
name|void
name|setLoadOnStartup
parameter_list|(
name|int
name|loadOnStartup
parameter_list|)
block|{
name|this
operator|.
name|loadOnStartup
operator|=
name|loadOnStartup
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

