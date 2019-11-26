begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * licensed to the apache software foundation (AS) under one  * or more contributor license agreements. see the notice file  * distributed with this work for additional information  * regarding copyright ownership. the asf licenses this file  * to you under the apache license, version 2.0 (the  * "license"); you may not use this file except in compliance  * with the license. you may obtain a copy of the license at  *  * http://www.apache.org/licenses/license-2.0  *  * unless required by applicable law or agreed to in writing,  * software distributed under the license is distributed on an  * "as is" basis, without warranties or conditions of any  * kind, either express or implied. see the license for the  * specific language governing permissions and limitations  * under the license.  */
end_comment

begin_package
package|package
name|sample
operator|.
name|ws
operator|.
name|service
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
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
name|test
operator|.
name|context
operator|.
name|SpringBootTest
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
name|test
operator|.
name|context
operator|.
name|SpringBootTest
operator|.
name|WebEnvironment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringRunner
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|UserTransaction
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|SpringBootTest
argument_list|(
name|classes
operator|=
name|SampleWsApplication
operator|.
name|class
argument_list|,
name|webEnvironment
operator|=
name|WebEnvironment
operator|.
name|DEFINED_PORT
argument_list|)
specifier|public
class|class
name|BridgeFromJTATest
block|{
annotation|@
name|Autowired
specifier|private
name|UserTransaction
name|ut
decl_stmt|;
specifier|private
name|FirstServiceAT
name|firstClient
decl_stmt|;
specifier|private
name|SecondServiceAT
name|secondClient
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|firstClient
operator|=
name|FirstClient
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|secondClient
operator|=
name|SecondClient
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|teardownTest
parameter_list|()
throws|throws
name|Exception
block|{
name|rollbackIfActive
argument_list|(
name|ut
argument_list|)
expr_stmt|;
try|try
block|{
name|ut
operator|.
name|begin
argument_list|()
expr_stmt|;
name|firstClient
operator|.
name|resetCounter
argument_list|()
expr_stmt|;
name|secondClient
operator|.
name|resetCounter
argument_list|()
expr_stmt|;
name|ut
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|rollbackIfActive
argument_list|(
name|ut
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|ut
operator|.
name|begin
argument_list|()
expr_stmt|;
name|firstClient
operator|.
name|incrementCounter
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|secondClient
operator|.
name|incrementCounter
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|ut
operator|.
name|commit
argument_list|()
expr_stmt|;
name|ut
operator|.
name|begin
argument_list|()
expr_stmt|;
name|int
name|counter1
init|=
name|firstClient
operator|.
name|getCounter
argument_list|()
decl_stmt|;
name|int
name|counter2
init|=
name|secondClient
operator|.
name|getCounter
argument_list|()
decl_stmt|;
name|ut
operator|.
name|commit
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|counter1
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|counter2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClientDrivenRollback
parameter_list|()
throws|throws
name|Exception
block|{
name|ut
operator|.
name|begin
argument_list|()
expr_stmt|;
name|firstClient
operator|.
name|incrementCounter
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|secondClient
operator|.
name|incrementCounter
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|ut
operator|.
name|rollback
argument_list|()
expr_stmt|;
name|ut
operator|.
name|begin
argument_list|()
expr_stmt|;
name|int
name|counter1
init|=
name|firstClient
operator|.
name|getCounter
argument_list|()
decl_stmt|;
name|int
name|counter2
init|=
name|secondClient
operator|.
name|getCounter
argument_list|()
decl_stmt|;
name|ut
operator|.
name|commit
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|counter1
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|counter2
argument_list|)
expr_stmt|;
block|}
comment|/**      * Utility method for rolling back a transaction if it is currently active.      *      * @param ut The User Business Activity to cancel.      */
specifier|private
name|void
name|rollbackIfActive
parameter_list|(
name|UserTransaction
name|ut
parameter_list|)
block|{
try|try
block|{
name|ut
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th2
parameter_list|)
block|{
comment|// do nothing, not active
block|}
block|}
block|}
end_class

end_unit
