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

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SystemPropertyAction
implements|implements
name|PrivilegedAction
argument_list|<
name|String
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|SystemPropertyAction
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|String
name|property
decl_stmt|;
specifier|private
specifier|final
name|String
name|def
decl_stmt|;
specifier|private
name|SystemPropertyAction
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|property
operator|=
name|name
expr_stmt|;
name|def
operator|=
literal|null
expr_stmt|;
block|}
specifier|private
name|SystemPropertyAction
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|d
parameter_list|)
block|{
name|property
operator|=
name|name
expr_stmt|;
name|def
operator|=
name|d
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see java.security.PrivilegedAction#run()      */
specifier|public
name|String
name|run
parameter_list|()
block|{
if|if
condition|(
name|def
operator|!=
literal|null
condition|)
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|property
argument_list|,
name|def
argument_list|)
return|;
block|}
return|return
name|System
operator|.
name|getProperty
argument_list|(
name|property
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|SystemPropertyAction
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|def
parameter_list|)
block|{
try|try
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|SystemPropertyAction
argument_list|(
name|name
argument_list|,
name|def
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"SecurityException raised getting property "
operator|+
name|name
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return
name|def
return|;
block|}
block|}
comment|/**      * Get the system property via the AccessController, but if a SecurityException is      * raised, just return null;      * @param name      */
specifier|public
specifier|static
name|String
name|getPropertyOrNull
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
return|return
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|SystemPropertyAction
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"SecurityException raised getting property "
operator|+
name|name
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

